package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player // Asegúrate de que esta importación exista si la necesitas para 'jugadores'
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.DeleteGameUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.GetAllPlayersUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.GetGameUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.UpsertGameCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.ValidationGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditGameViewModel @Inject constructor(
    private val getGameUseCase: GetGameUseCase,
    private val upsertGameUseCase: UpsertGameCase,
    private val deleteGameUseCase: DeleteGameUseCase,
    private val validationGameUseCase: ValidationGameUseCase,
    private val getAllPlayersUseCase: GetAllPlayersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EditGameUiState())
    val state: StateFlow<EditGameUiState> = _state.asStateFlow()

    fun onEvent(event: EditGameUiEvent) {
        when (event) {
            is EditGameUiEvent.Load -> onLoad(event.id)
            is EditGameUiEvent.FechaChanged -> onFechaChanged(event.value)
            is EditGameUiEvent.JugadorId1Changed -> onJugadorId1Changed(event.value)
            is EditGameUiEvent.JugadorId2Changed -> onJugadorId2Changed(event.value)
            is EditGameUiEvent.GanadorIdChanged -> onGanadorIdChanged(event.value)
            is EditGameUiEvent.EsFinalizadaChanged -> onEsFinalizadaChanged(event.value)
            EditGameUiEvent.Save -> onSave()
            EditGameUiEvent.Delete -> onDelete()
        }
    }

    private fun loadJugadores() {
        viewModelScope.launch {
            val jugadores = getAllPlayersUseCase()
            _state.update { it.copy(jugadores = jugadores) }
        }
    }

    private fun onLoad(gameId: Int?) {
        if (gameId == null || gameId == 0) {
            _state.update { it.copy(isNew = true, id = null) }
            loadJugadores()
            return
        }
        viewModelScope.launch {
            val game = getGameUseCase(gameId)
            if (game != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        id = game.Gameid,
                        fecha = game.Fecha,
                        jugadorId1 = game.JugadorId1,
                        jugadorId2 = game.JugadorId2,
                        ganadorId = game.GanadorId,
                        esFinalizada = game.EsFinalizada,
                    )
                }
            }
            loadJugadores()
        }
    }

    private fun onFechaChanged(fecha: String) {
        _state.update {
            it.copy(
                fecha = fecha,
                fechaError = if (fecha.isBlank()) "La fecha es requerida" else null
            )
        }
    }

    private fun onJugadorId1Changed(jugadorId: Int) {
        _state.update {
            it.copy(
                jugadorId1 = jugadorId,
                jugadorId1error = if (jugadorId == 0) "Jugador 1 requerido" else null
            )
        }
    }

    private fun onJugadorId2Changed(jugadorId: Int) {
        _state.update {
            it.copy(
                jugadorId2 = jugadorId,
                jugadorId2error = if (jugadorId == 0) "Jugador 2 requerido" else null
            )
        }
    }

    private fun onGanadorIdChanged(ganadorId: Int?) {
        _state.update {
            it.copy(
                ganadorId = ganadorId ?: 0,
                ganadorIderror = null
            )
        }
    }

    private fun onEsFinalizadaChanged(esFinalizada: Boolean) {
        _state.update { it.copy(esFinalizada = esFinalizada) }
    }

    private fun onSave() {
        viewModelScope.launch {
            val currentState = _state.value
            var isValid = true

            if (currentState.fecha.isBlank()) {
                _state.update { it.copy(fechaError = "La fecha es requerida") }
                isValid = false
            }
            if (currentState.jugadorId1 == 0) {
                _state.update { it.copy(jugadorId1error = "Jugador 1 requerido") }
                isValid = false
            }
            if (currentState.jugadorId2 == 0) {
                _state.update { it.copy(jugadorId2error = "Jugador 2 requerido") }
                isValid = false
            }
            if (currentState.jugadorId1 != 0 && currentState.jugadorId1 == currentState.jugadorId2) {
                _state.update { it.copy(jugadorId1error = "Los jugadores deben ser diferentes", jugadorId2error = "Los jugadores deben ser diferentes") }
                isValid = false
            }
            if (currentState.esFinalizada && currentState.ganadorId != 0 &&
                currentState.ganadorId != currentState.jugadorId1 && currentState.ganadorId != currentState.jugadorId2) {
                _state.update { it.copy(ganadorIderror = "El ganador debe ser uno de los jugadores") }
                isValid = false
            }

            if (!isValid) {
                _state.update { it.copy(isSaving = false) }
                return@launch
            }


            _state.update { it.copy(isSaving = true) }
            try {
                val game = Game(
                    Gameid = currentState.id ?: 0,
                    Fecha = currentState.fecha,
                    JugadorId1 = currentState.jugadorId1,
                    JugadorId2 = currentState.jugadorId2,
                    GanadorId = currentState.ganadorId,
                    EsFinalizada = currentState.esFinalizada
                )

                upsertGameUseCase(game)
                _state.update {
                    it.copy(
                        isSaving = false,
                        isGameSaved = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        fechaError = "Error al guardar: ${e.message}"
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val gameId = _state.value.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteGameUseCase(gameId)
                _state.update { it.copy(isDeleting = false, isGameDeleted = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        fechaError = "Error al eliminar: ${e.message}"
                    )
                }
            }
        }
    }
}
