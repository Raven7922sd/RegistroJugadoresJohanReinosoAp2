package com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase

import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import jakarta.inject.Inject

class GetAllPlayersUseCase @Inject constructor(
    private val repository: PlayerRepository

){
    suspend operator fun invoke() = repository.getAllPlayers()
}