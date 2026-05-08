package com.pokedex.app.domain.model

data class Pokemon(
    val id: Int,
    val nameFr: String,
    val typePrimary: String,
    val typeSecondary: String?,
    val weightKg: Float,
    val heightM: Float,
    val spriteUrl: String,
    val spriteShinyUrl: String,
    val isCaught: Boolean = false,
    val isShinyCaught: Boolean = false,
    val availableInGames: List<String> = emptyList(),
    val officialArtworkUrl: String? = null
)
