package com.pokedex.app.data.local.entity

import androidx.room.Entity

@Entity(tableName = "evolution_edge", primaryKeys = ["fromId", "toId"])
data class EvolutionEdgeEntity(
    val fromId: Int,
    val toId: Int,
    val conditions: String   // pre-formatted French text
)
