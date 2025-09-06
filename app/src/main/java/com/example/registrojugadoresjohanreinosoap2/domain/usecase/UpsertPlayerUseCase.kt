package com.example.registrojugadoresjohanreinosoap2.domain.usecase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository

class UpsertPlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(player: Player): Result<Int> {

        if (player.Nombres.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }

        if (player.Nombres.length>50){
            return Result.failure(IllegalArgumentException("El nombre no puede tener más de 50 caracteres"))
        }

        if (player.Partidas < 0) {
            return Result.failure(IllegalArgumentException("Las partidas no pueden ser negativas"))
        }

        return runCatching {
            repository.upsert(player)
        }
    }
}
