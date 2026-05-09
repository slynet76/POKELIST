package com.pokedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "variant_ability",
    primaryKeys = ["variantId", "abilityName"],
    indices = [Index("variantId")]
)
data class VariantAbilityEntity(
    val variantId: Int,
    val abilityName: String,
    val isHidden: Boolean,
    val slot: Int
)
