package com.pokedex.app.domain.model

/**
 * One Pokémon in an evolution chain, plus the condition to reach it from its predecessor.
 * If [conditionFromPredecessor] is null, this is the base form (no predecessor).
 */
data class EvolutionEntry(
    val pokemonId: Int,
    val nameFr: String,
    val spriteUrl: String,
    val conditionFromPredecessor: String?
)
