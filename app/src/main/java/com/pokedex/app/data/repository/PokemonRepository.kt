package com.pokedex.app.data.repository

import com.pokedex.app.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getAllPokemonFlow(): Flow<List<Pokemon>>
    fun getPokemonByGameFlow(gameCode: String): Flow<List<Pokemon>>
    suspend fun getPokemonById(id: Int): Pokemon?
    suspend fun toggleCaught(pokemonId: Int)
    suspend fun toggleShinyCaught(pokemonId: Int)
    suspend fun needsInitialSync(): Boolean
    suspend fun syncAllPokemon(onProgress: (Int, Int) -> Unit)
    suspend fun backgroundRefreshIfNeeded()
}
