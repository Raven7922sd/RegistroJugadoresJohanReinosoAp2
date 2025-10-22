package com.example.registrojugadoresjohanreinosoap2.data.remoteApi.Mapper

import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.dto.JugadorDto
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player

fun JugadorDto.toDomain(): Player = Player(
    Jugadorid = jugadorId ?: 0,
    Nombres = nombres,
    Partidas = 0
)

fun Player.toDto(): JugadorDto = JugadorDto(
    jugadorId = if (Jugadorid == 0) null else Jugadorid,
    nombres = Nombres,
    email = ""
)