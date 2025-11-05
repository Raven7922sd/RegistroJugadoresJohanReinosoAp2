package com.example.registrojugadoresjohanreinosoap2.domain.repository

import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun observeGame(): Flow<List<Game>>

    suspend fun getGame(id:Int): Game?

    fun getPartidasEnCurso(): Flow<List<Game>>

    suspend fun getAllGames(): List<Game>

    suspend fun upsert(game: Game):Int

    suspend fun delete(id:Int)
}