package com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import javax.inject.Inject

class UpsertGameCase @Inject constructor(
    private val GameRepository: GameRepository
) {
    suspend operator fun invoke(game: Game): Result<Int> {

        if (game.Fecha.isBlank()){
            return Result.failure(IllegalArgumentException("La fecha no puede estar vacía"))
        }
        if (game.JugadorId1 <= 0) {
            return Result.failure(IllegalArgumentException("El ID del Jugador 1 no puede ser menor o igual a 0"))
        }
        if (game.JugadorId2 <= 0) {
            return Result.failure(IllegalArgumentException("El ID del Jugador 2 no puede ser menor o igual a 0"))
        }

        if (game.JugadorId1 == game.JugadorId2) {
            return Result.failure(IllegalArgumentException("El Jugador 1 y el Jugador 2 no pueden ser el mismo"))
        }
        return runCatching {
            GameRepository.upsert(game)
        }
    }
}