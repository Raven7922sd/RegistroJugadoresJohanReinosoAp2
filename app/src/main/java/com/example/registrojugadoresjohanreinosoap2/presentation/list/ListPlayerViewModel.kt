package com.example.registrojugadoresjohanreinosoap2.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.DeletePlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.ObservePlayersUseCase
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
    private val deletePlayerUseCase: DeletePlayerUseCase
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

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deletePlayerUseCase(id)
                onEvent(ListPlayerUiEvent.ShowMessage("Jugador eliminado"))
            } catch (e: Exception) {
                onEvent(ListPlayerUiEvent.ShowMessage("Error al eliminar: ${e.message}"))
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