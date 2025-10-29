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
        suspend fun getById(id: String): PlayerEntity?

    @Query("SELECT*FROM jugadores WHERE LOWER(Nombres) = LOWER(:nombre)")
    suspend fun getByName(nombre: String): List<PlayerEntity>

    @Upsert
    suspend fun upsert(player: PlayerEntity):Long

    @Query("SELECT * FROM Jugadores WHERE Nombres = :nombre")
    suspend fun getPlayersByName(nombre: String): List<PlayerEntity>

    @Delete
    suspend fun delete(entity: PlayerEntity)

    @Query("DELETE FROM jugadores WHERE Jugadorid=:id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM jugadores")
    suspend fun getAllPlayers(): List<PlayerEntity>


    @Query("SELECT * FROM Jugadores WHERE isPendingCreate = 1")
    suspend fun getPendingCreateJugadores(): List<PlayerEntity>


    @Query("SELECT * FROM Jugadores WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int): PlayerEntity?
}