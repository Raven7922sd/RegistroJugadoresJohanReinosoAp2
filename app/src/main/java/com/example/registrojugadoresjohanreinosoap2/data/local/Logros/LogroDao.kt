package com.example.registrojugadoresjohanreinosoap2.data.local.Logros

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {
    @Query("SELECT*FROM Logros ORDER BY logroId DESC")
    fun observerAll(): Flow<List<LogroEntity>>

    @Query("SELECT*FROM Logros WHERE logroId=:id")
    suspend fun getById(id:Int): LogroEntity?

    @Upsert
    suspend fun upsert(logro: LogroEntity):Long

    @Delete
    suspend fun delete(entity: LogroEntity)

    @Query("DELETE FROM Logros WHERE logroId=:id")
    suspend fun deleteById(id:Int)
}