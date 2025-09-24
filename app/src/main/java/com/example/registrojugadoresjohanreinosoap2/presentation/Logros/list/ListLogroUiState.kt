package edu.ucne.registrojugadores.Presentation.Logros.List

import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros

data class ListLogroUiState(
    val isLoading: Boolean = false,
    val logros: List<Logros> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)