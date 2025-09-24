package com.example.registrojugadoresjohanreinosoap2.domain.usecase.logroUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveLogroUseCase @Inject constructor(
    val repository: LogroRepository
){
    operator fun invoke(): Flow<List<Logros>>{
        return repository.observeLogro()
    }
}