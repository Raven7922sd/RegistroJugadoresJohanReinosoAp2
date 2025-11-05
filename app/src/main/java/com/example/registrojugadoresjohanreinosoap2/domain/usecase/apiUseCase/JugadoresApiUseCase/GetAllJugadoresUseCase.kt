package com.example.registrojugadoresjohanreinosoap2.domain.usecase.apiUseCase.JugadoresApiUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.JugadorApiRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject

class GetAllJugadoresUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(): List<Player> {
        return repository.getAllPlayers()
    }
}