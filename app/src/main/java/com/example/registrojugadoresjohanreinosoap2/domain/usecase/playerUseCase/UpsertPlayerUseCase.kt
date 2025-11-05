package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject
import kotlin.compareTo

class UpsertPlayerUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(player: Player) {
        if (player.Nombres.isBlank()) {
            throw IllegalArgumentException("El nombre no puede estar vacío")
        }

        if (player.Nombres.length > 50) {
            throw IllegalArgumentException("El nombre no puede tener más de 50 caracteres")
        }

        if (player.Partidas < 0) {
            throw IllegalArgumentException("Las partidas no pueden ser negativas")
        }

        repository.upsert(player)
    }
}
