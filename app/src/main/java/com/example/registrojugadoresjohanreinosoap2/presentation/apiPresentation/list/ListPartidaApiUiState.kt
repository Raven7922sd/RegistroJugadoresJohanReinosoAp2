package com.example.registrojugadoresjohanreinosoap2.presentation.apiPresentation.list

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game

data class ListPartidaApiUiState(
    val isLoading: Boolean = false,
    val partidas: List<Game> = emptyList(),
    val message: String? = null,
    val navigateToGame: Int? = null
)

