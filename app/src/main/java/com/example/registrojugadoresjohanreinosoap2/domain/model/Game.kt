package com.example.registrojugadoresjohanreinosoap2.domain.model

class Game (
    val Gameid: Int =0,
    val Fecha: String ="",
    val JugadorId1: Int,
    val JugadorId2: Int,
    val GanadorId: Int,
    val EsFinalizada: Boolean =false
){
}