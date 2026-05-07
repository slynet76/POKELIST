package com.pokedex.app.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwitchGamesLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val data: Map<String, List<Int>> by lazy {
        val json = context.assets.open("switch_games.json").bufferedReader().readText()
        Gson().fromJson(json, object : TypeToken<Map<String, List<Int>>>() {}.type)
    }

    fun getIdsForGame(gameCode: String): List<Int> = data[gameCode] ?: emptyList()

    fun getGamesForPokemon(pokemonId: Int): List<String> =
        data.entries.filter { pokemonId in it.value }.map { it.key }
}
