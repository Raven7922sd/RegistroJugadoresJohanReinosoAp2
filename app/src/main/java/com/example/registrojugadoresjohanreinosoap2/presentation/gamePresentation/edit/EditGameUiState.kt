package com.example.registrojugadoresjohanreinosoap2.presentation.gamePresentation.edit

import com.example.registrojugadoresjohanreinosoap2.data.db.PlayerDb
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player

data class EditGameUiState(

    val id: Int? = null,
    val fecha: String = "",
    val jugadorId1: Int = 0,
    val jugadorId2: Int = 0,
    val ganadorId: Int? = null,
    val fechaError: String? = null,
    val jugadorId1error: String? = null,
    val jugadorId2error: String? = null,
    val ganadorIderror: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isGameSaved: Boolean = false,
    val isDeleting: Boolean = false,
    val isGameDeleted: Boolean = false,
    val isNew: Boolean = true,
    val esFinalizada: Boolean = false,
    val jugadoresDisponibles: List<Player> = emptyList(),

    val board: List<Player?> = List(9) { null },

    val winner: Player? = null,
    val isDraw: Boolean = false,
    val playerSelection: Player? = null,
    val gameStarted: Boolean = false,
    val message: String? = null,

    val selectedPlayerX: PlayerDb? = null,
    val selectedPlayerO: PlayerDb? = null,
    val selectionError: String? = null

)