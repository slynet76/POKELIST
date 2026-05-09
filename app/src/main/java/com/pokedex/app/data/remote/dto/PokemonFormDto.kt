package com.pokedex.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonFormDto(
    val id: Int,
    val name: String,
    @SerializedName("form_name") val formName: String?,
    val sprites: PokemonFormSpritesDto = PokemonFormSpritesDto(),
    val types: List<TypeSlotDto> = emptyList()
)

data class PokemonFormSpritesDto(
    @SerializedName("front_default") val frontDefault: String? = null,
    @SerializedName("front_shiny") val frontShiny: String? = null,
    val other: SpritesOtherDto? = null
)
