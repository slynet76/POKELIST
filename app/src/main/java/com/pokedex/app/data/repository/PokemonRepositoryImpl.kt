package com.pokedex.app.data.repository

import com.pokedex.app.data.local.dao.CaptureStatusDao
import com.pokedex.app.data.local.dao.EvolutionEdgeDao
import com.pokedex.app.data.local.dao.PokemonDao
import com.pokedex.app.data.local.entity.CaptureStatusEntity
import com.pokedex.app.data.local.entity.EvolutionEdgeEntity
import com.pokedex.app.data.local.entity.PokemonEntity
import com.pokedex.app.data.remote.EvolutionConditionFormatter
import com.pokedex.app.data.remote.PokeApiService
import com.pokedex.app.data.remote.dto.ChainLinkDto
import com.pokedex.app.domain.model.EvolutionEntry
import com.pokedex.app.domain.model.Pokemon
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

    override suspend fun syncAllPokemon(onProgress: (Int, Int) -> Unit) {
        val total = 1025
        val semaphore = Semaphore(10)
        val done = AtomicInteger(0)
        val processedChains = Collections.synchronizedSet(mutableSetOf<Int>())
        coroutineScope {
            (1..total).map { id ->
                async {
                    semaphore.withPermit {
                        runCatching { fetchAndCache(id, processedChains) }
                        onProgress(done.incrementAndGet(), total)
                    }
                }
            }.forEach { it.await() }
        }
        prefs.lastSyncTimestamp = System.currentTimeMillis()
    }

    override suspend fun backgroundRefreshIfNeeded() {
        if (prefs.needsSync() || needsEvolutionDataSync()) {
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
                conditionFromPredecessor = incoming?.conditions
            )
        }
    }

    private suspend fun fetchAndCache(id: Int, processedChains: MutableSet<Int>) {
        val dto = api.getPokemon(id)
        val species = api.getPokemonSpecies(id)
        val nameFr = species.names.find { it.language.name == "fr" }?.name ?: dto.name
        val primaryType = dto.types.find { it.slot == 1 }?.type?.name ?: "normal"
        val secondaryType = dto.types.find { it.slot == 2 }?.type?.name
        val chainId = species.evolutionChain?.url?.let { extractTrailingId(it) }

        pokemonDao.insertAll(listOf(
            PokemonEntity(
                id = id,
                nameFr = nameFr,
                typePrimary = primaryType,
                typeSecondary = secondaryType,
                weightKg = dto.weight / 10f,
                heightM = dto.height / 10f,
                spriteUrl = dto.sprites.frontDefault ?: "",
                spriteShinyUrl = dto.sprites.frontShiny ?: "",
                evolutionChainId = chainId
            )
        ))

        if (chainId != null && processedChains.add(chainId)) {
            runCatching {
                val chain = api.getEvolutionChain(chainId)
                val edges = mutableListOf<EvolutionEdgeEntity>()
                walkChain(chain.chain, edges)
                if (edges.isNotEmpty()) evolutionEdgeDao.insertAll(edges)
            }
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

    private fun mapToDomain(entities: List<PokemonEntity>, statuses: List<CaptureStatusEntity>): List<Pokemon> {
        val statusMap = statuses.associateBy { it.pokemonId }
        return entities.map { it.toDomain(statusMap[it.id], gamesLoader.getGamesForPokemon(it.id)) }
    }

    private fun PokemonEntity.toDomain(status: CaptureStatusEntity?, games: List<String>) = Pokemon(
        id = id, nameFr = nameFr, typePrimary = typePrimary, typeSecondary = typeSecondary,
        weightKg = weightKg, heightM = heightM, spriteUrl = spriteUrl, spriteShinyUrl = spriteShinyUrl,
        isCaught = status?.isCaught ?: false, isShinyCaught = status?.isShinyCaught ?: false,
        availableInGames = games
    )
}
