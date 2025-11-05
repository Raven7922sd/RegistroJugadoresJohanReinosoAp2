package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.DeleteGameUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.ObserveGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ListGameViewModel @Inject constructor(
    private val observeGamesUseCase: ObserveGameUseCase,
    private val deleteGameUseCase: DeleteGameUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ListGameUiState(isLoading = true))
    val state: StateFlow<ListGameUiState> = _state.asStateFlow()
    val players: StateFlow<List<Player>> = MutableStateFlow(emptyList())
    init {
        onEvent(ListGameUiEvent.Load)
    }

    fun onEvent(event: ListGameUiEvent) {
        when (event) {
            ListGameUiEvent.Load -> observeGames()
            is ListGameUiEvent.Delete -> onDelete(event.id)
            ListGameUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is ListGameUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is ListGameUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observeGames() {
        viewModelScope.launch {
            observeGamesUseCase().collectLatest { gamesList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        games = gamesList,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deleteGameUseCase(id)
                onEvent(ListGameUiEvent.ShowMessage("Partida eliminada"))
            } catch (e: Exception) {
                onEvent(ListGameUiEvent.ShowMessage("Error al eliminar: ${e.message}"))
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