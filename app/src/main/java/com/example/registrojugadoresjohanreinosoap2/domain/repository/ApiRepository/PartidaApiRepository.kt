package com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game

interface PartidaApiRepository {
    suspend fun getAllPartidas(): List<Game>
    suspend fun getPartida(id: Int): Game?
    suspend fun crearPartida(partida: Game): Game?
}