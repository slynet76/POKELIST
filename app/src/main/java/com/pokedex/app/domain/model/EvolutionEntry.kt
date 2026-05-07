package com.pokedex.app.domain.model

data class EvolutionEntry(
    val pokemonId: Int,
    val nameFr: String,
    val spriteUrl: String,
    val conditionFromPredecessor: String?,
    val formLabel: String? = null
)
