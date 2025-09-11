package com.example.registrojugadoresjohanreinosoap2.presentation.edit

sealed interface EditPlayerUiEvent {
    data class Load(val id: Int?) : EditPlayerUiEvent
    data class NameChanged(val value: String) : EditPlayerUiEvent

    data class PartidaChanged(val value: String) : EditPlayerUiEvent
    data object Save : EditPlayerUiEvent
    data object Delete : EditPlayerUiEvent
}