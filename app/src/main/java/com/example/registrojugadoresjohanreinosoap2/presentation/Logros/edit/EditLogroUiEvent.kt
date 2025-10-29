package edu.ucne.registrojugadores.Presentation.Logros.Edit

sealed interface EditLogroUiEvent {
    data class Load(val id: Int?) : EditLogroUiEvent
    data class NombreChanged(val value: String) : EditLogroUiEvent
    data class DescripcionChanged(val value: String) : EditLogroUiEvent
    data class esLogradoChanged(val value: Boolean) : EditLogroUiEvent
    data object Save : EditLogroUiEvent
    data object Delete : EditLogroUiEvent
}