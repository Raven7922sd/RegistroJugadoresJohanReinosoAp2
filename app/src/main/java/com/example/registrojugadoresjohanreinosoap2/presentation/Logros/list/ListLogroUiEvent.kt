package edu.ucne.registrojugadores.Presentation.Logros.List

sealed interface ListLogroUiEvent {
    data object Load: ListLogroUiEvent
    data class Delete(val id: Int) : ListLogroUiEvent
    data object CreateNew: ListLogroUiEvent
    data class Edit(val id: Int) : ListLogroUiEvent
    data class ShowMessage (val message: String) : ListLogroUiEvent
}