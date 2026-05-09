package com.pokedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ability")
data class AbilityEntity(
    @PrimaryKey val name: String,        // "overgrow"
    val nameFr: String,                  // "Engrais"
    val descriptionFr: String?           // localized flavor text, can be null
)
