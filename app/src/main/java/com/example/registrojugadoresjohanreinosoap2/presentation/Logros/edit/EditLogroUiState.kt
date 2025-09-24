package edu.ucne.registrojugadores.Presentation.Logros.Edit


data class EditLogroUiState(
    val LogroId: Int? = null,
    val NombreLogro: String = "",
    val Descripcion: String = "",
    val EsDesbloqueado: Boolean = false,
    val nombreError: String? = null,
    val descripcionError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)