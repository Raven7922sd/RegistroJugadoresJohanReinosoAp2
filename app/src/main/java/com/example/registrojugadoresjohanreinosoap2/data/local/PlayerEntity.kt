package com.example.registrojugadoresjohanreinosoap2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Jugadores")
data class PlayerEntity (
    @PrimaryKey
    val Jugadorid: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val Nombres: String,
    val Partidas: Int,
    val isPendingCreate: Boolean = false
)
{}