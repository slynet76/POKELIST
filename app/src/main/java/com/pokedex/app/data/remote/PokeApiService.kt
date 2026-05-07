package com.pokedex.app.data.remote

import com.pokedex.app.data.remote.dto.EvolutionChainDto
import com.pokedex.app.data.remote.dto.PokemonDto
import com.pokedex.app.data.remote.dto.PokemonSpeciesDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonDto

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpeciesDto

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") id: Int): EvolutionChainDto
}
