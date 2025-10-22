package com.example.registrojugadoresjohanreinosoap2.domain.usecase.apiUseCase.JugadoresApiUseCase


import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.JugadorApiRepository
import javax.inject.Inject

class PostJugadoresUseCase @Inject constructor(
    private val repository: JugadorApiRepository
) {
    suspend operator fun invoke(jugador: Player): Player? {
        return repository.crearJugador(jugador)
    }
}