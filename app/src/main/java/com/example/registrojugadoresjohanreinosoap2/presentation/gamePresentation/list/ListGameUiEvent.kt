package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list

import com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit.EditGameUiEvent

interface ListGameUiEvent {
    data object Load: ListGameUiEvent
    data class Delete(val id: Int) : ListGameUiEvent
    data object CreateNew: ListGameUiEvent
    data class Edit(val id: Int) : ListGameUiEvent
    data class ShowMessage(val message: String) : ListGameUiEvent

    data class FechaChanged(val value: String) : ListGameUiEvent

    data class JugadorId1Changed(val value: Int) : ListGameUiEvent

    data class JugadorId2Changed(val value: Int) : ListGameUiEvent

    data class GanadorIdChanged(val value: Int?) : ListGameUiEvent

    data class EsFinalizadaChanged(val value: Boolean) : ListGameUiEvent
    object NavigateToGame : ListGameUiEvent
}