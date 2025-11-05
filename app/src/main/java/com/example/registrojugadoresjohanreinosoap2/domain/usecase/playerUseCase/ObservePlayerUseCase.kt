package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePlayersUseCase@Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<List<Player>> {
        return repository.observePlayer()
    }
}