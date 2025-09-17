package com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import jakarta.inject.Inject

class DeleteGameUseCase @Inject constructor(
    private val repository: GameRepository) {

    suspend operator fun invoke(id: Int) {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        repository.delete(id)
    }
}