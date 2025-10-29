package com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import jakarta.inject.Inject

class DeleteLogroUseCase @Inject constructor(
    private val repository: LogroRepository) {

    suspend operator fun invoke(id: Int) {
        if (id <= 0) throw IllegalArgumentException("El id debe ser mayor que 0")
        repository.delete(id)
    }
}