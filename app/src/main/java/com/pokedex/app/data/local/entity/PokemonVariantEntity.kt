package com.pokedex.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One form of a Pokémon species. Each species has at least one variant (the "default" form).
 * Some species (Miaouss, Slowpoke, etc.) have multiple variants for regional forms.
 *
 * - [variantId] is the PokéAPI Pokemon resource ID (e.g. 52 for default Meowth, 10161 for Galar Meowth).
 * - [speciesId] is the National Dex number (1-1025), shared by all variants of a species.
 * - [formName] is the PokéAPI form key ("default", "galar", "alola", "hisui", etc.).
 * - [formLabelFr] is the user-facing French label ("Forme Galar", null for default).
 */
@Entity(
    tableName = "pokemon_variant",
    indices = [Index(value = ["speciesId"])]
)
data class PokemonVariantEntity(
    @PrimaryKey val variantId: Int,
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
    val animatedShinySpriteUrl: String? = null
)
