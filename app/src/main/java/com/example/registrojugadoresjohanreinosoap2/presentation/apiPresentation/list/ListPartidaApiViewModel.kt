package com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.apiUseCase.JugadoresApiUseCase.GetAllPartidasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPartidaApiViewModel @Inject constructor(
    private val getPartidasUseCase: GetAllPartidasUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListPartidaApiUiState(isLoading = true))
    val state: StateFlow<ListPartidaApiUiState> = _state.asStateFlow()

    private val _eventFlow = MutableStateFlow<ListPartidaApiUiEvent?>(null)
    val eventFlow = _eventFlow.asStateFlow()

    init {
        onEvent(ListPartidaApiUiEvent.Load)
    }

    fun onEvent(event: ListPartidaApiUiEvent) {
        when(event) {
            ListPartidaApiUiEvent.Load -> loadPartidas()
            ListPartidaApiUiEvent.NavigateToCreate -> {
                _eventFlow.value = ListPartidaApiUiEvent.NavigateToCreate
            }
            is ListPartidaApiUiEvent.NavigateToGame -> {
                _eventFlow.value = ListPartidaApiUiEvent.NavigateToGame(event.partidaId)
            }
            is ListPartidaApiUiEvent.ShowMessage -> {
                _state.update { it.copy(message = event.message) }
            }
        }
    }

    private fun loadPartidas() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val partidas = getPartidasUseCase()
                _state.update {
                    it.copy(
                        isLoading = false,
                        partidas = partidas,
                        message = "${partidas.size} partidas cargadas"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        message = "Error al cargar partidas: ${e.message}"
                    )
                }
            }
        }
    }

    fun onEventConsumed() {
        _eventFlow.value = null
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                message = null
            )
        }
    }
}