package com.pokedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val nameFr: String,
    val typePrimary: String,
    val typeSecondary: String?,
    val weightKg: Float,
    val heightM: Float,
    val spriteUrl: String,
    val spriteShinyUrl: String
)
