package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject

class DeletePlayerUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(id: String) {
        if (id.isBlank()) throw IllegalArgumentException("El ID debe ser mayor que 0")
        repository.delete(id)
    }
}