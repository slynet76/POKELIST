package com.pokedex.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesDto(
    val names: List<LocalizedNameDto>,
    @SerializedName("evolution_chain") val evolutionChain: NamedResourceDto? = null,
    val varieties: List<VarietyDto> = emptyList()
)

data class LocalizedNameDto(
    val name: String,
    val language: NamedResourceDto
)

data class VarietyDto(
    @SerializedName("is_default") val isDefault: Boolean,
    val pokemon: NamedResourceDto
)
