package com.pokedex.app.ui.navigation

sealed class Screen(val route: String) {
    object Loading : Screen("loading")
    object PokedexList : Screen("list")
    object PokemonDetail : Screen("detail/{pokemonId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}
