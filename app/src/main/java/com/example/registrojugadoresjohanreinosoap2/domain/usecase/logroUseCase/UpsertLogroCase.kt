package com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import javax.inject.Inject

class UpsertLogroCase @Inject constructor(
    private val logrorepository: LogroRepository
) {
    suspend operator fun invoke(logro: Logros): Result<Int> {

        if (logro.LogroNombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre es requerido"))
        }

        if (logro.Descripcion.isBlank()) {
            return Result.failure(IllegalArgumentException("La descripcion es requerida"))
        }

        return runCatching {
            logrorepository.upsert(logro)
        }
    }
}