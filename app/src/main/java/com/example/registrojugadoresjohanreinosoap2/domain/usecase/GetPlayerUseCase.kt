package com.example.registrojugadoresjohanreinosoap2.domain.usecase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository

class GetPlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(id: Int): Player? {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        return repository.getPlayer(id)
    }
}