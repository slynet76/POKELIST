package com.pokedex.app.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.util.TypeChart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val weaknesses: List<String> = emptyList(),
    val resistances: List<String> = emptyList(),
    val immunities: List<String> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState

    init { loadPokemon() }

    private fun loadPokemon() = viewModelScope.launch {
        val pokemon = repository.getPokemonById(pokemonId)
        if (pokemon != null) {
            _uiState.value = PokemonDetailUiState(
                pokemon = pokemon,
                weaknesses = TypeChart.getWeaknesses(pokemon.typePrimary, pokemon.typeSecondary),
                resistances = TypeChart.getResistances(pokemon.typePrimary, pokemon.typeSecondary),
                immunities = TypeChart.getImmunities(pokemon.typePrimary, pokemon.typeSecondary),
                isLoading = false
            )
        }
    }

    fun toggleCaught() = viewModelScope.launch {
        repository.toggleCaught(pokemonId)
        loadPokemon()
    }

    fun toggleShinyCaught() = viewModelScope.launch {
        repository.toggleShinyCaught(pokemonId)
        loadPokemon()
    }
}
