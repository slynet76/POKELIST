package com.pokedex.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val types: List<TypeSlotDto>,
    val sprites: SpritesDto,
    val stats: List<StatDto> = emptyList(),
    val cries: CriesDto? = null,
    val abilities: List<AbilitySlotDto> = emptyList(),
    val forms: List<NamedResourceDto> = emptyList()
)

data class AbilitySlotDto(
    val ability: NamedResourceDto,
    @SerializedName("is_hidden") val isHidden: Boolean,
    val slot: Int
)

data class TypeSlotDto(
    val slot: Int,
    val type: NamedResourceDto
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_shiny")   val frontShiny: String?,
    val other: SpritesOtherDto? = null
)

data class NamedResourceDto(val name: String, val url: String)

data class StatDto(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedResourceDto
)

data class CriesDto(
    val latest: String? = null,
    val legacy: String? = null
)

data class SpritesOtherDto(
    val showdown: ShowdownSpritesDto? = null,
    @SerializedName("official-artwork") val officialArtwork: OfficialArtworkSpritesDto? = null
)

data class ShowdownSpritesDto(
    @SerializedName("front_default") val frontDefault: String? = null,
    @SerializedName("front_shiny") val frontShiny: String? = null
)

data class OfficialArtworkSpritesDto(
    @SerializedName("front_default") val frontDefault: String? = null,
    @SerializedName("front_shiny") val frontShiny: String? = null
)
