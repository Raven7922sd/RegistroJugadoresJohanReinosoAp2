package com.example.registrojugadoresjohanreinosoap2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registrojugadoresjohanreinosoap2.data.local.GameDao
import com.example.registrojugadoresjohanreinosoap2.data.local.GameEntity
import com.example.registrojugadoresjohanreinosoap2.data.local.Logros.LogroDao
import com.example.registrojugadoresjohanreinosoap2.data.local.Logros.LogroEntity
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerEntity

@Database(entities = [PlayerEntity::class, GameEntity::class, LogroEntity::class],
    version = 4,
    exportSchema = false)

abstract class PlayerDb: RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun GameDao(): GameDao

    abstract fun LogroDao(): LogroDao
}