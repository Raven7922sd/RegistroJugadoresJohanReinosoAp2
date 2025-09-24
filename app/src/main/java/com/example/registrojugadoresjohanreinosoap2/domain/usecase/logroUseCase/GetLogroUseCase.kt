package com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import jakarta.inject.Inject

class GetLogroUseCase @Inject constructor(
    private val repository: LogroRepository
)
{
    suspend operator fun invoke(id: Int): Logros? {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        return repository.getLogro(id)
    }
}