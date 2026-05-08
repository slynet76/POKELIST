package com.pokedex.app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SyncState {
    object Checking : SyncState()
    data class Syncing(val progress: Int, val total: Int) : SyncState()
    object Done : SyncState()
}

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SyncState>(SyncState.Checking)
    val state: StateFlow<SyncState> = _state

    init {
        viewModelScope.launch {
            val needsFullSync = repository.needsInitialSync() ||
                repository.needsEvolutionDataSync() ||
                repository.needsVariantsSync() ||
                repository.needsV14DataSync() ||
                repository.needsArtworkSync()
            if (needsFullSync) {
                _state.value = SyncState.Syncing(0, 1025)
                repository.syncAllPokemon { done, total ->
                    _state.value = SyncState.Syncing(done, total)
                }
            } else {
                repository.backgroundRefreshIfNeeded()
            }
            _state.value = SyncState.Done
        }
    }
}
