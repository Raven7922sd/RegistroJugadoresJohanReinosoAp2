package com.example.registrojugadoresjohanreinosoap2.presentation.Players.list

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player

data class ListPlayerUiState(
    val isLoading: Boolean = false,
    val players: List<Player> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val isSyncing: Boolean = false,
    val isDownloading: Boolean = false,
    val navigateToEditId: String? = null
) {
}