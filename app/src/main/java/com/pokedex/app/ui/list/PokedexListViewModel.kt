package com.pokedex.app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.SwitchGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokedexListUiState(
    val pokemon: List<Pokemon> = emptyList(),
    val selectedGame: SwitchGame = SwitchGame.ALL,
    val searchQuery: String = "",
    val caughtCount: Int = 0,
    val shinyCaughtCount: Int = 0,
    val totalCount: Int = 0
)

@HiltViewModel
class PokedexListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _selectedGame = MutableStateFlow(SwitchGame.ALL)
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _pokemon: StateFlow<List<Pokemon>> = _selectedGame.flatMapLatest { game ->
        if (game == SwitchGame.ALL) repository.getAllPokemonFlow()
        else repository.getPokemonByGameFlow(game.code)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val uiState: StateFlow<PokedexListUiState> = combine(
        _pokemon, _selectedGame, _searchQuery
    ) { pokemon, game, query ->
        val filtered = if (query.isBlank()) pokemon
                       else pokemon.filter { it.nameFr.contains(query, ignoreCase = true) }
        PokedexListUiState(
            pokemon = filtered,
            selectedGame = game,
            searchQuery = query,
            caughtCount = pokemon.count { it.isCaught },
            shinyCaughtCount = pokemon.count { it.isShinyCaught },
            totalCount = pokemon.size
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, PokedexListUiState())

    fun selectGame(game: SwitchGame) { _selectedGame.value = game }
    fun search(query: String) { _searchQuery.value = query }
    fun toggleCaught(pokemonId: Int) = viewModelScope.launch { repository.toggleCaught(pokemonId) }
    fun toggleShinyCaught(pokemonId: Int) = viewModelScope.launch { repository.toggleShinyCaught(pokemonId) }
}
