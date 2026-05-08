package com.pokedex.app.domain.model

data class PokemonForm(
    val variantId: Int,
    val speciesId: Int,
    val formName: String,
    val formLabelFr: String?,
    val nameFr: String,
    val typePrimary: String,
    val typeSecondary: String?,
    val weightKg: Float,
    val heightM: Float,
    val spriteUrl: String,
    val spriteShinyUrl: String,
    val isDefault: Boolean
)
