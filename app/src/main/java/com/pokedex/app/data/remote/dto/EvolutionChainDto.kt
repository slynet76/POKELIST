package com.pokedex.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EvolutionChainDto(
    val id: Int,
    val chain: ChainLinkDto
)

data class ChainLinkDto(
    val species: NamedResourceDto,
    @SerializedName("evolution_details") val evolutionDetails: List<EvolutionDetailDto> = emptyList(),
    @SerializedName("evolves_to") val evolvesTo: List<ChainLinkDto> = emptyList()
)

data class EvolutionDetailDto(
    @SerializedName("min_level") val minLevel: Int? = null,
    @SerializedName("min_happiness") val minHappiness: Int? = null,
    @SerializedName("min_affection") val minAffection: Int? = null,
    @SerializedName("min_beauty") val minBeauty: Int? = null,
    val item: NamedResourceDto? = null,
    @SerializedName("held_item") val heldItem: NamedResourceDto? = null,
    @SerializedName("known_move") val knownMove: NamedResourceDto? = null,
    val location: NamedResourceDto? = null,
    val trigger: NamedResourceDto? = null,
    @SerializedName("time_of_day") val timeOfDay: String? = null,
    val gender: Int? = null,
    @SerializedName("needs_overworld_rain") val needsOverworldRain: Boolean = false,
    @SerializedName("turn_upside_down") val turnUpsideDown: Boolean = false
)
