package com.pokedex.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pokedex.app.ui.loading.LoadingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Loading.route) {
        composable(Screen.Loading.route) {
            LoadingScreen(progress = 0, total = 1025)
        }
        composable(Screen.PokedexList.route) {
            // Filled in Task 13
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) {
            // Filled in Task 16
        }
    }
}
