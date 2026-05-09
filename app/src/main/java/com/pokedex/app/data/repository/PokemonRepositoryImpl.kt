package com.pokedex.app.data.repository

import com.pokedex.app.data.local.dao.AbilityDao
import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.EvolutionEdgeDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.dao.PokemonVariantDao
import com.pokedex.app.data.local.dao.VariantAbilityDao
import com.pokedex.app.data.local.entity.AbilityEntity
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.EvolutionEdgeEntity
import com.pokedex.app.data.local.entity.PokemonEntity
import com.pokedex.app.data.local.entity.PokemonVariantEntity
import com.pokedex.app.data.local.entity.VariantAbilityEntity
import com.pokedex.app.data.remote.EvolutionConditionFormatter
import com.pokedex.app.data.remote.FormLabels
import com.pokedex.app.data.remote.PokeApiService
import com.pokedex.app.data.remote.dto.ChainLinkDto
import com.pokedex.app.data.remote.dto.PokemonDto
import com.pokedex.app.domain.model.EvolutionEntry
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.PokemonAbility
import com.pokedex.app.domain.model.PokemonForm
import com.pokedex.app.domain.util.RegionalFormLabels
import com.pokedex.app.util.PreferencesManager
import com.pokedex.app.util.SwitchGamesLoader
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val captureStatusDao: CaptureStatusDao,
    private val evolutionEdgeDao: EvolutionEdgeDao,
    private val pokemonVariantDao: PokemonVariantDao,
    private val abilityDao: AbilityDao,
    private val variantAbilityDao: VariantAbilityDao,
    private val api: PokeApiService,
    private val prefs: PreferencesManager,
    private val gamesLoader: SwitchGamesLoader
) : PokemonRepository {

    override fun getAllPokemonFlow(): Flow<List<Pokemon>> =
        combine(pokemonDao.getAllFlow(), captureStatusDao.getAllFlow()) { entities, statuses ->
            mapToDomain(entities, statuses)
        }

    override fun getPokemonByGameFlow(gameCode: String): Flow<List<Pokemon>> {
        val ids = gamesLoader.getIdsForGame(gameCode)
        return combine(pokemonDao.getByIdsFlow(ids), captureStatusDao.getAllFlow()) { entities, statuses ->
            mapToDomain(entities, statuses)
        }
    }

    override suspend fun getPokemonById(id: Int): Pokemon? {
        val entity = pokemonDao.getById(id) ?: return null
        val status = captureStatusDao.getById(id)
        return entity.toDomain(status, gamesLoader.getGamesForPokemon(id))
    }

    override suspend fun toggleCaught(pokemonId: Int) {
        val current = captureStatusDao.getById(pokemonId)
        captureStatusDao.upsert(
            current?.copy(isCaught = !current.isCaught)
                ?: CaptureStatusEntity(pokemonId = pokemonId, isCaught = true)
        )
    }

    override suspend fun toggleShinyCaught(pokemonId: Int) {
        val current = captureStatusDao.getById(pokemonId)
        captureStatusDao.upsert(
            current?.copy(isShinyCaught = !current.isShinyCaught)
                ?: CaptureStatusEntity(pokemonId = pokemonId, isShinyCaught = true)
        )
    }

    override suspend fun needsInitialSync(): Boolean = pokemonDao.count() == 0

    override suspend fun needsEvolutionDataSync(): Boolean = pokemonDao.countMissingEvolutionData() > 0

    override suspend fun needsVariantsSync(): Boolean = pokemonVariantDao.count() == 0

    override suspend fun needsV14DataSync(): Boolean = pokemonVariantDao.countMissingV14Data() > 0

    override suspend fun needsArtworkSync(): Boolean = pokemonDao.countMissingArtwork() > 0

    override suspend fun needsAbilitiesSync(): Boolean = abilityDao.count() == 0 || variantAbilityDao.count() == 0

    override suspend fun getForms(speciesId: Int): List<PokemonForm> =
        pokemonVariantDao.getVariantsForSpecies(speciesId).map { it.toDomain() }

    override suspend fun getAbilitiesForVariant(variantId: Int): List<PokemonAbility> {
        val rows = variantAbilityDao.getForVariant(variantId)
        if (rows.isEmpty()) return emptyList()
        val names = rows.map { it.abilityName }.distinct()
        val abilities = abilityDao.getByNames(names).associateBy { it.name }
        return rows.mapNotNull { row ->
            val a = abilities[row.abilityName] ?: return@mapNotNull null
            PokemonAbility(
                name = a.name,
                nameFr = a.nameFr,
                descriptionFr = a.descriptionFr,
                isHidden = row.isHidden,
                slot = row.slot
            )
        }
    }

    override suspend fun syncAllPokemon(onProgress: (Int, Int) -> Unit) {
        val total = 1025
        val semaphore = Semaphore(10)
        val done = AtomicInteger(0)
        val processedChains = Collections.synchronizedSet(mutableSetOf<Int>())
        val processedAbilities = Collections.synchronizedSet(mutableSetOf<String>())
        coroutineScope {
            (1..total).map { id ->
                async {
                    semaphore.withPermit {
                        runCatching { fetchAndCache(id, processedChains, processedAbilities) }
                        onProgress(done.incrementAndGet(), total)
                    }
                }
            }.forEach { it.await() }
        }
        prefs.lastSyncTimestamp = System.currentTimeMillis()
    }

    override suspend fun backgroundRefreshIfNeeded() {
        if (prefs.needsSync() || needsEvolutionDataSync() || needsVariantsSync() || needsV14DataSync() || needsArtworkSync() || needsAbilitiesSync()) {
            syncAllPokemon(onProgress = { _, _ -> })
        }
    }

    override suspend fun getEvolutionEntries(pokemonId: Int): List<EvolutionEntry> {
        val pokemon = pokemonDao.getById(pokemonId) ?: return emptyList()
        val chainId = pokemon.evolutionChainId ?: return emptyList()
        val ids = pokemonDao.getIdsInChain(chainId).sorted()
        return ids.mapNotNull { id ->
            val entity = pokemonDao.getById(id) ?: return@mapNotNull null
            val edges = evolutionEdgeDao.getEdgesForPokemon(id)
            val incoming = edges.firstOrNull { it.toId == id }
            EvolutionEntry(
                pokemonId = entity.id,
                nameFr = entity.nameFr,
                spriteUrl = entity.spriteUrl,
                conditionFromPredecessor = incoming?.conditions,
                formLabel = RegionalFormLabels.forId(entity.id)
            )
        }
    }

    private suspend fun fetchAndCache(id: Int, processedChains: MutableSet<Int>, processedAbilities: MutableSet<String>) {
        val defaultDto = api.getPokemon(id)
        val species = api.getPokemonSpecies(id)
        val nameFr = species.names.find { it.language.name == "fr" }?.name ?: defaultDto.name
        val primaryType = defaultDto.types.find { it.slot == 1 }?.type?.name ?: "normal"
        val secondaryType = defaultDto.types.find { it.slot == 2 }?.type?.name
        val chainId = species.evolutionChain?.url?.let { extractTrailingId(it) }

        pokemonDao.insertAll(listOf(
            PokemonEntity(
                id = id,
                nameFr = nameFr,
                typePrimary = primaryType,
                typeSecondary = secondaryType,
                weightKg = defaultDto.weight / 10f,
                heightM = defaultDto.height / 10f,
                spriteUrl = defaultDto.sprites.frontDefault ?: "",
                spriteShinyUrl = defaultDto.sprites.frontShiny ?: "",
                evolutionChainId = chainId,
                officialArtworkUrl = defaultDto.sprites.other?.officialArtwork?.frontDefault
            )
        ))

        // Variantes (formes régionales, méga, gigamax, etc.)
        val variants = mutableListOf<PokemonVariantEntity>()
        val variantDtos = mutableListOf<PokemonDto>()
        val speciesEnName = defaultDto.name
        for (variety in species.varieties) {
            val variantName = variety.pokemon.name
            if (FormLabels.shouldSkipVariety(variantName)) continue

            val variantDto = if (variety.isDefault) defaultDto
            else runCatching { api.getPokemonByName(variantName) }.getOrNull() ?: continue

            val formSuffix = if (variety.isDefault) "default"
            else variantName.removePrefix("$speciesEnName-").ifEmpty { variantName }

            variantDtos += variantDto
            variants += PokemonVariantEntity(
                variantId = variantDto.id,
                speciesId = id,
                formName = formSuffix,
                formLabelFr = FormLabels.formatFor(formSuffix),
                nameFr = nameFr,
                typePrimary = variantDto.types.find { it.slot == 1 }?.type?.name ?: "normal",
                typeSecondary = variantDto.types.find { it.slot == 2 }?.type?.name,
                weightKg = variantDto.weight / 10f,
                heightM = variantDto.height / 10f,
                spriteUrl = variantDto.sprites.frontDefault ?: defaultDto.sprites.frontDefault ?: "",
                spriteShinyUrl = variantDto.sprites.frontShiny ?: defaultDto.sprites.frontShiny ?: "",
                isDefault = variety.isDefault,
                hp = statByName(variantDto.stats, "hp"),
                attack = statByName(variantDto.stats, "attack"),
                defense = statByName(variantDto.stats, "defense"),
                specialAttack = statByName(variantDto.stats, "special-attack"),
                specialDefense = statByName(variantDto.stats, "special-defense"),
                speed = statByName(variantDto.stats, "speed"),
                cryUrl = variantDto.cries?.latest,
                animatedSpriteUrl = variantDto.sprites.other?.showdown?.frontDefault,
                animatedShinySpriteUrl = variantDto.sprites.other?.showdown?.frontShiny,
                officialArtworkUrl = variantDto.sprites.other?.officialArtwork?.frontDefault,
                officialArtworkShinyUrl = variantDto.sprites.other?.officialArtwork?.frontShiny
            )
        }
        if (variants.isNotEmpty()) pokemonVariantDao.insertAll(variants)

        // Talents/abilities: collect rows per variant + dedup ability fetches.
        val abilityRows = mutableListOf<VariantAbilityEntity>()
        val abilityNamesToFetch = mutableSetOf<String>()
        for (variantDto in variantDtos) {
            for (slotDto in variantDto.abilities) {
                val abilityName = slotDto.ability.name
                abilityRows += VariantAbilityEntity(
                    variantId = variantDto.id,
                    abilityName = abilityName,
                    isHidden = slotDto.isHidden,
                    slot = slotDto.slot
                )
                if (processedAbilities.add(abilityName)) {
                    abilityNamesToFetch += abilityName
                }
            }
        }
        if (abilityRows.isNotEmpty()) variantAbilityDao.insertAll(abilityRows)
        for (abilityName in abilityNamesToFetch) {
            fetchAndStoreAbility(abilityName)
        }

        if (chainId != null && processedChains.add(chainId)) {
            runCatching {
                val chain = api.getEvolutionChain(chainId)
                val edges = mutableListOf<EvolutionEdgeEntity>()
                walkChain(chain.chain, edges)
                if (edges.isNotEmpty()) evolutionEdgeDao.insertAll(edges)
            }
        }
    }

    private suspend fun fetchAndStoreAbility(name: String) {
        runCatching {
            val dto = api.getAbility(name)
            val nameFr = dto.names.firstOrNull { it.language.name == "fr" }?.name
                ?: name.split('-').joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            val descriptionFr = dto.flavorTextEntries.firstOrNull { it.language.name == "fr" }?.flavorText
                ?.replace('\n', ' ')
                ?.replace('', ' ')
            abilityDao.insertAll(listOf(AbilityEntity(name = name, nameFr = nameFr, descriptionFr = descriptionFr)))
        }
    }

    private fun walkChain(link: ChainLinkDto, out: MutableList<EvolutionEdgeEntity>) {
        val fromId = extractTrailingId(link.species.url) ?: return
        link.evolvesTo.forEach { child ->
            val toId = extractTrailingId(child.species.url) ?: return@forEach
            val condition = child.evolutionDetails.firstOrNull()
                ?.let { EvolutionConditionFormatter.format(it) }
                ?: "Évolution spéciale"
            out += EvolutionEdgeEntity(fromId = fromId, toId = toId, conditions = condition)
            walkChain(child, out)
        }
    }

    private fun extractTrailingId(url: String): Int? =
        url.trimEnd('/').substringAfterLast('/').toIntOrNull()

    private fun statByName(stats: List<com.pokedex.app.data.remote.dto.StatDto>, name: String): Int =
        stats.firstOrNull { it.stat.name == name }?.baseStat ?: 0

    private fun mapToDomain(entities: List<PokemonEntity>, statuses: List<CaptureStatusEntity>): List<Pokemon> {
        val statusMap = statuses.associateBy { it.pokemonId }
        return entities.map { it.toDomain(statusMap[it.id], gamesLoader.getGamesForPokemon(it.id)) }
    }

    private fun PokemonEntity.toDomain(status: CaptureStatusEntity?, games: List<String>) = Pokemon(
        id = id, nameFr = nameFr, typePrimary = typePrimary, typeSecondary = typeSecondary,
        weightKg = weightKg, heightM = heightM, spriteUrl = spriteUrl, spriteShinyUrl = spriteShinyUrl,
        isCaught = status?.isCaught ?: false, isShinyCaught = status?.isShinyCaught ?: false,
        availableInGames = games,
        officialArtworkUrl = officialArtworkUrl
    )

    private fun PokemonVariantEntity.toDomain() = PokemonForm(
        variantId = variantId,
        speciesId = speciesId,
        formName = formName,
        formLabelFr = formLabelFr,
        nameFr = nameFr,
        typePrimary = typePrimary,
        typeSecondary = typeSecondary,
        weightKg = weightKg,
        heightM = heightM,
        spriteUrl = spriteUrl,
        spriteShinyUrl = spriteShinyUrl,
        isDefault = isDefault,
        hp = hp,
        attack = attack,
        defense = defense,
        specialAttack = specialAttack,
        specialDefense = specialDefense,
        speed = speed,
        cryUrl = cryUrl,
        animatedSpriteUrl = animatedSpriteUrl,
        animatedShinySpriteUrl = animatedShinySpriteUrl,
        officialArtworkUrl = officialArtworkUrl,
        officialArtworkShinyUrl = officialArtworkShinyUrl
    )
}
