package com.example.registrojugadoresjohanreinosoap2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Partidas")
class GameEntity (
    @PrimaryKey (autoGenerate = true )
    val Gameid: Int=0,
    val Fecha: String="",
    val JugadorId1: Int,
    val JugadorId2: Int,
    val GanadorId: Int,
    val EsFinalizada: Boolean=false
){
}