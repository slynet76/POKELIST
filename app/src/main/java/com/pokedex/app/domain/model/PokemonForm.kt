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
    val isDefault: Boolean,
    val hp: Int = 0,
    val attack: Int = 0,
    val defense: Int = 0,
    val specialAttack: Int = 0,
    val specialDefense: Int = 0,
    val speed: Int = 0,
    val cryUrl: String? = null,
    val animatedSpriteUrl: String? = null,
    val animatedShinySpriteUrl: String? = null,
    val officialArtworkUrl: String? = null,
    val officialArtworkShinyUrl: String? = null,
    val abilities: List<PokemonAbility> = emptyList()
)
