package com.example.registrojugadoresjohanreinosoap2.domain.usecase.apiUseCase.JugadoresApiUseCase


import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.PartidaApiRepository
import javax.inject.Inject

class GetAllPartidasUseCase @Inject constructor(
    private val repository: PartidaApiRepository
) {
    suspend operator fun invoke(): List<Game> {
        return repository.getAllPartidas()
    }
}