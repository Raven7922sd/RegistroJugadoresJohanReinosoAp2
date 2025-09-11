package com.example.registrojugadoresjohanreinosoap2.presentation.list

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player

data class ListPlayerUiState(
    val isLoading: Boolean = false,
    val players: List<Player> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
) {
}