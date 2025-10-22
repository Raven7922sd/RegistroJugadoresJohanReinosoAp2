package com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation.list

sealed interface ListPartidaApiUiEvent {
    data object Load: ListPartidaApiUiEvent
    data class ShowMessage (val message: String) : ListPartidaApiUiEvent
    object NavigateToCreate : ListPartidaApiUiEvent
    data class NavigateToGame(val partidaId: Int) : ListPartidaApiUiEvent
}