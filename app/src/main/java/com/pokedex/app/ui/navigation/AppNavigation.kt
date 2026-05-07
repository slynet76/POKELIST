package com.pokedex.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pokedex.app.ui.detail.PokemonDetailScreen
import com.pokedex.app.ui.list.PokedexListScreen
import com.pokedex.app.ui.list.SyncState
import com.pokedex.app.ui.list.SyncViewModel
import com.pokedex.app.ui.loading.LoadingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Loading.route) {
        composable(Screen.Loading.route) {
            val syncVm: SyncViewModel = hiltViewModel()
            val syncState by syncVm.state.collectAsState()

            LaunchedEffect(syncState) {
                if (syncState is SyncState.Done) {
                    navController.navigate(Screen.PokedexList.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            }

            when (val s = syncState) {
                is SyncState.Syncing -> LoadingScreen(s.progress, s.total)
                else -> LoadingScreen(0, 1025)
            }
        }
        composable(Screen.PokedexList.route) {
            PokedexListScreen(onPokemonClick = { id ->
                navController.navigate(Screen.PokemonDetail.createRoute(id))
            })
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
        ) {
            PokemonDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
