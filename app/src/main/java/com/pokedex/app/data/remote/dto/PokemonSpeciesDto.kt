package com.pokedex.app.data.remote.dto

data class PokemonSpeciesDto(
    val names: List<LocalizedNameDto>
)

data class LocalizedNameDto(
    val name: String,
    val language: NamedResourceDto
)
