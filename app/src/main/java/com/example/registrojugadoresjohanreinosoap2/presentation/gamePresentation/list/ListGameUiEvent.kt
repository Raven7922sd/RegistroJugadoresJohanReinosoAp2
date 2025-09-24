package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list

interface ListGameUiEvent {
    data object Load: ListGameUiEvent
    data class Delete(val id: Int) : ListGameUiEvent
    data object CreateNew: ListGameUiEvent
    data class Edit(val id: Int) : ListGameUiEvent
    data class ShowMessage(val message: String) : ListGameUiEvent
}