package com.pokedex.app.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pokedex.app.ui.list.components.GameFilterChips
import com.pokedex.app.ui.list.components.PokemonCard
import com.pokedex.app.ui.list.components.ProgressBarsSection
import com.pokedex.app.ui.theme.PokeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexListScreen(
    onPokemonClick: (Int) -> Unit,
    viewModel: PokedexListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            androidx.compose.foundation.layout.Column {
                TopAppBar(
                    title = { Text("PokéDex") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PokeRed,
                        titleContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = { showSearch = !showSearch }) {
                            Icon(Icons.Default.Search, contentDescription = "Rechercher", tint = Color.White)
                        }
                    }
                )
                if (showSearch) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = viewModel::search,
                        placeholder = { Text("Rechercher un Pokémon…") },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                        singleLine = true
                    )
                }
                GameFilterChips(
                    selectedGame = state.selectedGame,
                    onSelect = viewModel::selectGame,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        },
        bottomBar = {
            ProgressBarsSection(
                caughtCount = state.caughtCount,
                shinyCaughtCount = state.shinyCaughtCount,
                total = state.totalCount
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(padding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.pokemon, key = { it.id }) { pokemon ->
                PokemonCard(
                    pokemon = pokemon,
                    onClick = { onPokemonClick(pokemon.id) },
                    onToggleCaught = { viewModel.toggleCaught(pokemon.id) },
                    onToggleShinyCaught = { viewModel.toggleShinyCaught(pokemon.id) }
                )
            }
        }
    }
}
