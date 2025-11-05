package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
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
    private val getAllPlayersUseCase: GetAllPlayersUseCase,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _state = MutableStateFlow(EditGameUiState())
    val state: StateFlow<EditGameUiState> = _state.asStateFlow()

    fun onEvent(event: EditGameUiEvent) {
        when (event) {
            is EditGameUiEvent.Load -> onLoad(event.id)
            is EditGameUiEvent.FechaChanged -> onFechaChanged(event.value)
            is EditGameUiEvent.JugadorId1Changed -> onJugador1Changed(event.value)
            is EditGameUiEvent.JugadorId2Changed -> onJugador2Changed(event.value)
            is EditGameUiEvent.GanadorIdChanged -> onGanadorChanged(event.value)
            is EditGameUiEvent.EsFinalizadaChanged -> onFinalizadaChanged(event.value)
            EditGameUiEvent.Save -> onSave()
            EditGameUiEvent.Delete -> onDelete()
        }
    }

    private fun loadJugadores() {
        viewModelScope.launch {
            val jugadores = getAllPlayersUseCase()
            _state.update { it.copy(jugadoresDisponibles = jugadores) }
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
                        ganadorId = game.GanadorId?: 0,
                        esFinalizada = game.EsFinalizada,
                    )
                }
            }
            loadJugadores()
        }
    }

    private fun onJugador1Changed(jugadorId: Int){
        viewModelScope.launch {
            val jugadores = playerRepository.getAllPlayers()
            val jugador = jugadores.find { it.remoteId == jugadorId }
            _state.update {
                it.copy(
                    jugadorId1 = jugadorId,
                    jugadorId1error = null
                )
            }
        }
    }

    private fun onJugador2Changed(jugadorId: Int){
        viewModelScope.launch {
            val jugadores = playerRepository.getAllPlayers()
            val jugador = jugadores.find { it.remoteId == jugadorId }
            _state.update {
                it.copy(
                    jugadorId2 = jugadorId,
                    jugadorId2error = null
                )
            }
        }
    }

    private fun onFechaChanged(fecha: String){
        _state.update {
            it.copy(
                fecha = fecha,
                fechaError = if(fecha.isBlank()) "Fecha requerida" else null
            )
        }
    }

    private fun onGanadorChanged(ganadorId: Int?){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    ganadorId = ganadorId
                )
            }
        }
    }

    private fun onFinalizadaChanged(finalizada: Boolean){
        _state.update { it.copy(esFinalizada = finalizada) }
    }

    private fun onSave(){
        viewModelScope.launch {
            if(_state.value.fecha.isBlank()){
                _state.update { it.copy(fechaError = "Fecha requerida") }
                return@launch
            }
            if(_state.value.jugadorId1 == 0){
                _state.update { it.copy(jugadorId1error = "Jugador 1 requerido") }
                return@launch
            }
            if(_state.value.jugadorId2 == 0){
                _state.update { it.copy(jugadorId2error = "Jugador 2 requerido") }
                return@launch
            }
            if(_state.value.jugadorId1 == _state.value.jugadorId2){
                _state.update { it.copy(jugadorId1error = "Jugadores no pueden ser iguales", jugadorId2error = "Jugadores no pueden ser iguales") }
                return@launch
            }
            _state.update { it.copy(isSaving = true) }

            try{
                val partida = Game(
                    Gameid = _state.value.id ?: 0,
                    Fecha = _state.value.fecha,
                    JugadorId1 = _state.value.jugadorId1,
                    JugadorId2 = _state.value.jugadorId2,
                    GanadorId = _state.value.ganadorId,
                    EsFinalizada = _state.value.esFinalizada
                )

                upsertGameUseCase(partida)

                _state.update {
                    it.copy(
                        isSaving = false,
                        isGameSaved = true)
                }

            }catch (e: Exception){
                _state.update {
                    it.copy(
                        isSaving = false,
                        fechaError = "Error al guardar: ${e.message}"
                    )
                }
            }
        }
    }
    private fun onDelete(){
        val id = _state.value.id?: return

        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteGameUseCase(id)
                _state.update { it.copy(isDeleting = false, isGameDeleted = true) }
            }catch (e: Exception){
                _state.update {
                    it.copy(
                        isDeleting = false,
                        fechaError = "Error al eliminar: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadJugadoresDisponibles(){
        viewModelScope.launch {
            val jugadores = playerRepository.getAllPlayers()
            _state.update { it.copy(jugadoresDisponibles = jugadores) }
        }
    }
}
