package com.example.registrojugadoresjohanreinosoap2.presentation.Players.list

sealed interface ListPlayerUiEvent{
    data object Load: ListPlayerUiEvent
    data class Delete(val id: String) : ListPlayerUiEvent
    data object CreateNew: ListPlayerUiEvent
    data class Edit(val id: String) : ListPlayerUiEvent
    data class ShowMessage(val message: String) : ListPlayerUiEvent

    data object SyncPending : ListPlayerUiEvent
    data object DownloadFromApi : ListPlayerUiEvent
}