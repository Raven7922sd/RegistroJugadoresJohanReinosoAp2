package com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import jakarta.inject.Inject

class ValidationGameUseCase @Inject constructor(
    private val repository: GameRepository) {

    data class Validation(
        val isValid: Boolean,
        val fechaError: String? = null,
        val jugadorId1Error: String? = null,
        val jugadorId2Error: String? = null,
        val ganadorIdError: String? = null
    )

    suspend operator fun invoke(
        fecha: String,
        jugadorId1: Int?,
        jugadorId2: Int?,
        ganadorId: Int?
    ): Validation {

        val fechaError = when {
            fecha.isBlank() -> "La fecha es requerida"
            else -> null
        }

        val jugadorId1Error = when {
            jugadorId1 == null || jugadorId1 <= 0 -> "El jugador 1 es requerido"
            else -> null
        }

        val jugadorId2Error = when {
            jugadorId2 == null || jugadorId2 <= 0 -> "El jugador 2 es requerido"
            jugadorId2 == jugadorId1 -> "El jugador 2 debe ser diferente al jugador 1"
            else -> null
        }

        val ganadorIdError = when {
            ganadorId == null || ganadorId <= 0 -> "El ganador es requerido"
            ganadorId != jugadorId1 && ganadorId != jugadorId2 -> "El ganador debe ser uno de los jugadores"
            else -> null
        }

        return Validation(
            isValid = fechaError == null &&
                    jugadorId1Error == null &&
                    jugadorId2Error == null &&
                    ganadorIdError == null,
            fechaError = fechaError,
            jugadorId1Error = jugadorId1Error,
            jugadorId2Error = jugadorId2Error,
            ganadorIdError = ganadorIdError
        )
    }
}