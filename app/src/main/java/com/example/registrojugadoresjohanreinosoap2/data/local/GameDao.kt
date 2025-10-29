package com.example.registrojugadoresjohanreinosoap2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT*FROM partidas ORDER BY Gameid DESC")
        fun observerAll(): Flow<List<GameEntity>>

    @Query("SELECT*FROM partidas WHERE Gameid=:id")
        suspend fun getById(id:Int): GameEntity?

    @Upsert
    suspend fun upsert(game: GameEntity):Long

    @Delete
    suspend fun delete(entity: GameEntity)

    @Query("DELETE FROM partidas WHERE Gameid=:id")
    suspend fun deleteById(id:Int)

    @Query("SELECT * FROM partidas")
    suspend fun getAllGames(): List<GameEntity>

    @Query("SELECT * FROM partidas WHERE esFinalizada = 0")
    fun getPartidasEnCurso(): Flow<List<Game>>
}