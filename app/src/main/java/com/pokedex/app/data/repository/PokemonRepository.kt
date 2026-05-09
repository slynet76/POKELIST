package com.pokedex.app.data.repository

import com.pokedex.app.domain.model.EvolutionEntry
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.PokemonAbility
import com.pokedex.app.domain.model.PokemonForm
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getAllPokemonFlow(): Flow<List<Pokemon>>
    fun getPokemonByGameFlow(gameCode: String): Flow<List<Pokemon>>
    suspend fun getPokemonById(id: Int): Pokemon?
    suspend fun toggleCaught(pokemonId: Int)
    suspend fun toggleShinyCaught(pokemonId: Int)
    suspend fun needsInitialSync(): Boolean
    suspend fun needsEvolutionDataSync(): Boolean
    suspend fun needsVariantsSync(): Boolean
    suspend fun needsV14DataSync(): Boolean
    suspend fun needsArtworkSync(): Boolean
    suspend fun needsAbilitiesSync(): Boolean
    suspend fun getEvolutionEntries(pokemonId: Int): List<EvolutionEntry>
    suspend fun getForms(speciesId: Int): List<PokemonForm>
    suspend fun getAbilitiesForVariant(variantId: Int): List<PokemonAbility>
    suspend fun syncAllPokemon(onProgress: (Int, Int) -> Unit)
    suspend fun backgroundRefreshIfNeeded()
}
