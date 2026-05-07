package com.pokedex.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val types: List<TypeSlotDto>,
    val sprites: SpritesDto
)

data class TypeSlotDto(
    val slot: Int,
    val type: NamedResourceDto
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_shiny")   val frontShiny: String?
)

data class NamedResourceDto(val name: String, val url: String)
