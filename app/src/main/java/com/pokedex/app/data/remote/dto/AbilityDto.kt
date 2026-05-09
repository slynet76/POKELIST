package com.pokedex.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AbilityDto(
    val name: String,
    val names: List<LocalizedNameDto> = emptyList(),
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<AbilityFlavorTextDto> = emptyList()
)

data class AbilityFlavorTextDto(
    @SerializedName("flavor_text") val flavorText: String,
    val language: NamedResourceDto
)
