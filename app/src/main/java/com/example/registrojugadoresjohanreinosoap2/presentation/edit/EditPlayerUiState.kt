package com.example.registrojugadoresjohanreinosoap2.presentation.edit

data class EditPlayerUiState(
    val id: Int? = null,
    val name: String = "",
    val gamesPlayed: Int? = null,
    val nameError: String? = null,
    val gamesPlayedError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isPlayerSaved: Boolean = false,
    val isDeleting: Boolean = false,
    val isPlayerDeleted: Boolean = false,
    val isNew: Boolean = true,
)