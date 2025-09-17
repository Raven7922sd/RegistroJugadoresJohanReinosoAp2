package com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import jakarta.inject.Inject

class GetGameUseCase @Inject constructor(
    private val repository: GameRepository)
{
    suspend operator fun invoke(id: Int): Game? {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        return repository.getGame(id)
    }
}