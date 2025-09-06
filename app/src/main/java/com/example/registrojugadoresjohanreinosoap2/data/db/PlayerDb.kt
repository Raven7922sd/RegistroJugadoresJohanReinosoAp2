package com.example.registrojugadoresjohanreinosoap2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerEntity

@Database(entities = [PlayerEntity::class],
    version = 1,
    exportSchema = false)

abstract class PlayerDb: RoomDatabase() {

    abstract fun playerDao(): PlayerDao
}