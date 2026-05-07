package com.pokedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "capture_status")
data class CaptureStatusEntity(
    @PrimaryKey val pokemonId: Int,
    val isCaught: Boolean = false,
    val isShinyCaught: Boolean = false
)
