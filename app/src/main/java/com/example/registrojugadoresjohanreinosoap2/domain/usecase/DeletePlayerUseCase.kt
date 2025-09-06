package com.example.registrojugadoresjohanreinosoap2.domain.usecase

import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository

class DeletePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(id: Int) {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        repository.delete(id)
    }
}