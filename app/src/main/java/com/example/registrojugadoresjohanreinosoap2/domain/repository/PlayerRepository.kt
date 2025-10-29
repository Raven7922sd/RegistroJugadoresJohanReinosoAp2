package com.example.registrojugadoresjohanreinosoap2.domain.repository

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun observePlayer(): Flow<List<Player>>

    suspend fun getPlayer(id: String): Player?

    suspend fun getAllPlayers(): List<Player>

    suspend fun upsert(player: Player)

    suspend fun delete(id: String)

    suspend fun getPlayersByName(nombre: String): List<Player>

    suspend fun postPendingPlayers(): Boolean

    suspend fun cargarJugadoresApi(): Boolean

    suspend fun createPlayerLocal(player: Player): String
}
