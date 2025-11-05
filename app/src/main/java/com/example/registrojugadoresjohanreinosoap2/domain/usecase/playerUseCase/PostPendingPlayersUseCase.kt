package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject

class PostPendingPlayersUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.postPendingPlayers()
    }
}