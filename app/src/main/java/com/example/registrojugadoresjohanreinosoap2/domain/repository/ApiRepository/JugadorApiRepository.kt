package com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player

interface JugadorApiRepository {
    suspend fun getAllJugadores(): List<Player>
    suspend fun getJugador(id: Int): Player?
    suspend fun crearJugador(jugador: Player): Player?
}