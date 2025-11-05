package com.example.registrojugadoresjohanreinosoap2.presentation.Players.edit

sealed interface EditPlayerUiEvent {
    data class Load(val id: String?) : EditPlayerUiEvent
    data class NameChanged(val value: String) : EditPlayerUiEvent

    data class PartidaChanged(val value: String) : EditPlayerUiEvent
    data object Save : EditPlayerUiEvent
    data object Delete : EditPlayerUiEvent
}