package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject

class GetPlayerUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(id: String): Player? {
        if (id.isBlank()) throw IllegalArgumentException("El id debe ser mayor que 0")
        return repository.getPlayer(id)
    }
}