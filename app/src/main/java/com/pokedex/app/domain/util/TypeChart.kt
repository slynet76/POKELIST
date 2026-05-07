package com.pokedex.app.domain.util

object TypeChart {
    private val weakTo = mapOf(
        "normal"   to listOf("fighting"),
        "fire"     to listOf("water", "ground", "rock"),
        "water"    to listOf("electric", "grass"),
        "electric" to listOf("ground"),
        "grass"    to listOf("fire", "ice", "poison", "flying", "bug"),
        "ice"      to listOf("fire", "fighting", "rock", "steel"),
        "fighting" to listOf("flying", "psychic", "fairy"),
        "poison"   to listOf("ground", "psychic"),
        "ground"   to listOf("water", "grass", "ice"),
        "flying"   to listOf("electric", "ice", "rock"),
        "psychic"  to listOf("bug", "ghost", "dark"),
        "bug"      to listOf("fire", "flying", "rock"),
        "rock"     to listOf("water", "grass", "fighting", "ground", "steel"),
        "ghost"    to listOf("ghost", "dark"),
        "dragon"   to listOf("ice", "dragon", "fairy"),
        "dark"     to listOf("fighting", "bug", "fairy"),
        "steel"    to listOf("fire", "fighting", "ground"),
        "fairy"    to listOf("poison", "steel"),
    )

    private val resistantTo = mapOf(
        "normal"   to emptyList(),
        "fire"     to listOf("fire", "grass", "ice", "bug", "steel", "fairy"),
        "water"    to listOf("fire", "water", "ice", "steel"),
        "electric" to listOf("electric", "flying", "steel"),
        "grass"    to listOf("water", "electric", "grass", "ground"),
        "ice"      to listOf("ice"),
        "fighting" to listOf("bug", "rock", "dark"),
        "poison"   to listOf("grass", "fighting", "poison", "bug", "fairy"),
        "ground"   to listOf("poison", "rock"),
        "flying"   to listOf("grass", "fighting", "bug"),
        "psychic"  to listOf("fighting", "psychic"),
        "bug"      to listOf("grass", "fighting", "ground"),
        "rock"     to listOf("normal", "fire", "poison", "flying"),
        "ghost"    to listOf("poison", "bug"),
        "dragon"   to listOf("fire", "water", "electric", "grass"),
        "dark"     to listOf("ghost", "dark"),
        "steel"    to listOf("normal", "grass", "ice", "flying", "psychic", "bug", "rock", "dragon", "steel", "fairy"),
        "fairy"    to listOf("fighting", "bug", "dark"),
    )

    private val immuneTo = mapOf(
        "normal"   to listOf("ghost"),
        "fire"     to emptyList(),
        "water"    to emptyList(),
        "electric" to emptyList(),
        "grass"    to emptyList(),
        "ice"      to emptyList(),
        "fighting" to emptyList(),
        "poison"   to emptyList(),
        "ground"   to listOf("electric"),
        "flying"   to listOf("ground"),
        "psychic"  to emptyList(),
        "bug"      to emptyList(),
        "rock"     to emptyList(),
        "ghost"    to listOf("normal", "fighting"),
        "dragon"   to emptyList(),
        "dark"     to listOf("psychic"),
        "steel"    to listOf("poison"),
        "fairy"    to listOf("dragon"),
    )

    val ALL_TYPES = listOf(
        "normal", "fire", "water", "electric", "grass", "ice", "fighting",
        "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
        "dragon", "dark", "steel", "fairy"
    )

    fun getEffectiveness(attacking: String, defType1: String, defType2: String?): Float {
        var m = multiplier(attacking, defType1)
        if (defType2 != null) m *= multiplier(attacking, defType2)
        return m
    }

    private fun multiplier(attacking: String, defending: String): Float = when {
        immuneTo[defending]?.contains(attacking) == true    -> 0f
        weakTo[defending]?.contains(attacking) == true      -> 2f
        resistantTo[defending]?.contains(attacking) == true -> 0.5f
        else -> 1f
    }

    fun getWeaknesses(t1: String, t2: String?) =
        ALL_TYPES.filter { getEffectiveness(it, t1, t2) > 1f }

    fun getResistances(t1: String, t2: String?) =
        ALL_TYPES.filter { v -> getEffectiveness(v, t1, t2).let { it < 1f && it > 0f } }

    fun getImmunities(t1: String, t2: String?) =
        ALL_TYPES.filter { getEffectiveness(it, t1, t2) == 0f }
}
