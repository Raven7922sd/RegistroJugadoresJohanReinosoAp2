package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit

sealed interface EditGameUiEvent {
    data class Load(val id: Int?) : EditGameUiEvent
    data class FechaChanged(val value: String) : EditGameUiEvent
    data class JugadorId1Changed(val value: Int) : EditGameUiEvent
    data class JugadorId2Changed(val value: Int) : EditGameUiEvent
    data class GanadorIdChanged(val value: Int?) : EditGameUiEvent
    data class EsFinalizadaChanged(val value: Boolean) : EditGameUiEvent
    data object Save : EditGameUiEvent
    data object Delete : EditGameUiEvent
}
