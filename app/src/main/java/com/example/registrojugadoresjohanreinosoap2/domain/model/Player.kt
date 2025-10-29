package com.example.registrojugadoresjohanreinosoap2.domain.model

import java.util.UUID

class Player(
    val Jugadorid: String = UUID.randomUUID().toString(),
    val Nombres: String,
    val Partidas: Int,
    val remoteId: Int? = null,
){
}