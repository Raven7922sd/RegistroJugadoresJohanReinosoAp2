package com.example.registrojugadoresjohanreinosoap2.domain.repository

import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun observePlayer(): Flow<List<Player>>

    suspend fun getPlayer(id:Int): Player?

    suspend fun upsert(player: Player):Int

    suspend fun delete(id:Int)
}
