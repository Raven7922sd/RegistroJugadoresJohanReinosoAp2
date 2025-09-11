package com.example.registrojugadoresjohanreinosoap2.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.DeletePlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.GetPlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.UpsertPlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.ValidationPlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlayerViewModel @Inject constructor(
    private val getPlayerUseCase: GetPlayerUseCase,
    private val upsertPlayerUseCase: UpsertPlayerUseCase,
    private val deletePlayerUseCase: DeletePlayerUseCase,
    private val validationPlayerUseCase: ValidationPlayerUseCase
) : ViewModel(){
    private val _state = MutableStateFlow(EditPlayerUiState())
    val state: StateFlow<EditPlayerUiState> = _state.asStateFlow()

    private fun validateNombre(nombre: String) {
        viewModelScope.launch {
            val result = validationPlayerUseCase(
                nombre = nombre,
                partida = _state.value.gamesPlayed,
                currentPlayerId = _state.value.id
            )

            _state.update {
                it.copy(nameError = result.nombreError)
            }
        }
    }
    private fun validatePartida(partida: Int?) {
        viewModelScope.launch {
            val result = validationPlayerUseCase(
                nombre = _state.value.name,
                partida = partida,
                currentPlayerId = _state.value.id
            )

            _state.update {
                it.copy(gamesPlayedError = result.partidaError)
            }
        }
    }

    fun onEvent(event: EditPlayerUiEvent){
        when(event){
            is EditPlayerUiEvent.Load -> onLoad(event.id)
            is EditPlayerUiEvent.NameChanged ->{
                _state.update {
                    it.copy(
                        name = event.value,
                        nameError = null
                    )
                }
                validateNombre(event.value)
            }
            is EditPlayerUiEvent.PartidaChanged ->{
                val partidaInt = event.value.toIntOrNull()
                _state.update {
                    it.copy(
                        gamesPlayed = partidaInt,
                        gamesPlayedError = null
                    )
                }
                validatePartida(partidaInt)
            }
            EditPlayerUiEvent.Save -> onSave()
            EditPlayerUiEvent.Delete -> onDelete()
        }
    }
    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, id = null) }
            return
        }
        viewModelScope.launch {
            val player = getPlayerUseCase(id)
            if (player != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        id = player.Jugadorid,
                        name = player.Nombres,
                        gamesPlayed = player.Partidas
                    )
                }
            }
        }
    }
    private fun onSave() {
        viewModelScope.launch {
            val partidaInt = state.value.gamesPlayed ?: return@launch
            val validationResult = validationPlayerUseCase(
                nombre = _state.value.name,
                partida = _state.value.gamesPlayed,
                currentPlayerId = _state.value.id
            )

            if (!validationResult.isValid) {
                _state.update {
                    it.copy(
                        nameError = validationResult.nombreError,
                        gamesPlayedError = validationResult.partidaError,
                        isSaving = false
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            try {
                val player = Player(
                    Jugadorid = _state.value.id ?: 0,
                    Nombres = _state.value.name,
                    Partidas = partidaInt
                )

                upsertPlayerUseCase(player)
                _state.update {
                    it.copy(
                        isSaving = false,
                        isPlayerSaved = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nameError = "Error al guardar: ${e.message}"
                    )
                }
            }
        }
    }
    private fun onDelete() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deletePlayerUseCase(id)
                _state.update { it.copy(isDeleting = false, isPlayerDeleted = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        nameError = "Error al eliminar: ${e.message}"
                    )
                }
            }
        }
    }
}