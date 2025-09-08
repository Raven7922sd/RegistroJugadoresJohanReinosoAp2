package com.example.registrojugadoresjohanreinosoap2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Query("SELECT*FROM jugadores ORDER BY Jugadorid DESC")
        fun observerAll(): Flow<List<PlayerEntity>>

    @Query("SELECT*FROM jugadores WHERE Jugadorid=:id")
        suspend fun getById(id:Int): PlayerEntity?

    @Query("SELECT*FROM jugadores WHERE LOWER(Nombres) = LOWER(:nombre)")
    suspend fun getByName(nombre: String): List<PlayerEntity>

    @Upsert
    suspend fun upsert(player: PlayerEntity):Long

    @Delete
    suspend fun delete(entity: PlayerEntity)

    @Query("DELETE FROM jugadores WHERE Jugadorid=:id")
    suspend fun deleteById(id:Int)

    @Query("SELECT * FROM jugadores")
    suspend fun getAllPlayers(): List<PlayerEntity>
}