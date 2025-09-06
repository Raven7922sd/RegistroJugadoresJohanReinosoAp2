package com.example.registrojugadoresjohanreinosoap2.domain.usecase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class ObservePlayersUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<List<Player>> {
        return repository.observePlayer()
    }
}
