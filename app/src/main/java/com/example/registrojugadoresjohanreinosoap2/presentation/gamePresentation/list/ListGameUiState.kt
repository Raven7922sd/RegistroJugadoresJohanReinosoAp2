package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.list

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game

data class ListGameUiState (
    val isLoading: Boolean = false,
    val games: List<Game> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
) {
}