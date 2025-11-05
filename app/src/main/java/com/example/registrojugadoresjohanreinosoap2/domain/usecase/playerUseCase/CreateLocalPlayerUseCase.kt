package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject

class CreatePlayerLocalUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(jugador: Player): String {
        return repository.createPlayerLocal(jugador)
    }
}