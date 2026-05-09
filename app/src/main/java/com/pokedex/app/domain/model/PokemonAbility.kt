package com.pokedex.app.domain.model

data class PokemonAbility(
    val name: String,           // English internal id, e.g. "overgrow"
    val nameFr: String,         // "Engrais"
    val descriptionFr: String?,
    val isHidden: Boolean,
    val slot: Int
)
