package com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveGameUseCase @Inject constructor(
    val repository: GameRepository
){
    operator fun invoke(): Flow<List<Game>>{
        return repository.observeGame()
    }
}