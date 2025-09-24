package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.ObservePlayersUseCase
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