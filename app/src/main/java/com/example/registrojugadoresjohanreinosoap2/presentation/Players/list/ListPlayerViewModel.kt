package com.example.registrojugadoresjohanreinosoap2.presentation.Players.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase.DeletePlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase.DownloadPlayersUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase.ObservePlayersUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase.PostPendingPlayersUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase.TriggerSyncUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPlayerViewModel @Inject constructor(
    private val observePlayersUseCase: ObservePlayersUseCase,
    private val deletePlayerUseCase: DeletePlayerUseCase,
    private val postPendingPlayersUseCase: PostPendingPlayersUseCase,
    private val downloadPlayersUseCase: DownloadPlayersUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListPlayerUiState(isLoading = true))
    val state: StateFlow<ListPlayerUiState> = _state.asStateFlow()

    init {
        onEvent(ListPlayerUiEvent.Load)
    }

    fun onEvent(event: ListPlayerUiEvent) {
        when (event) {
            ListPlayerUiEvent.Load -> observePlayers()
            is ListPlayerUiEvent.Delete -> onDelete(event.id)
            ListPlayerUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is ListPlayerUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is ListPlayerUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            ListPlayerUiEvent.SyncPending -> onSyncPending()
            ListPlayerUiEvent.DownloadFromApi -> onDownloadFromApi()
        }
    }

    private fun observePlayers() {
        viewModelScope.launch {
            observePlayersUseCase().collectLatest { playersList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        players = playersList,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: String) {
        viewModelScope.launch {
            try {
                deletePlayerUseCase(id)
                onEvent(ListPlayerUiEvent.ShowMessage("Jugador eliminado"))
            } catch (e: Exception) {
                onEvent(ListPlayerUiEvent.ShowMessage("Error al eliminar: ${e.message}"))
            }
        }
    }

    private fun onSyncPending() {
        viewModelScope.launch {
            try {
                val success = postPendingPlayersUseCase()
                if (success) {
                    onEvent(ListPlayerUiEvent.ShowMessage("Jugadores pendientes sincronizados"))
                } else {
                    onEvent(ListPlayerUiEvent.ShowMessage("Error al sincronizar pendientes"))
                }
            } catch (e: Exception) {
                onEvent(ListPlayerUiEvent.ShowMessage("Error: ${e.message}"))
            }
        }
    }

    private fun onDownloadFromApi() {
        viewModelScope.launch {
            try {
                val success = downloadPlayersUseCase()
                if (success) {
                    onEvent(ListPlayerUiEvent.ShowMessage("Jugadores descargados de la API"))
                } else {
                    onEvent(ListPlayerUiEvent.ShowMessage("Error al descargar jugadores"))
                }
            } catch (e: Exception) {
                onEvent(ListPlayerUiEvent.ShowMessage("Error: ${e.message}"))
            }
        }
    }

    fun triggerAutoSync() {
        viewModelScope.launch {
            try {
                triggerSyncUseCase()
                onEvent(ListPlayerUiEvent.ShowMessage("Sincronización automática iniciada"))
            } catch (e: Exception) {
                onEvent(ListPlayerUiEvent.ShowMessage("Error al sincronizar: ${e.message}"))
            }
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null,
                message = null
            )
        }
    }
}