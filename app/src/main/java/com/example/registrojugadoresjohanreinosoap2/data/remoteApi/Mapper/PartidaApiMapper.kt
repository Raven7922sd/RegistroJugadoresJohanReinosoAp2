package com.example.registrojugadoresjohanreinosoap2.data.remoteApi.Mapper

import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.dto.PartidaDto
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game

fun PartidaDto.toDomain(): Game = Game(
    Gameid =  partidaId ?: 0,
    Fecha = "",
    JugadorId1 = jugador1Id,
    JugadorId2 = jugador2Id,
    GanadorId  = null,
    EsFinalizada = false,
    tablero = "",
    jugadorActual = "X"
)

fun Game.toDto(): PartidaDto = PartidaDto(
    partidaId = if (Gameid == 0) null else Gameid,
    jugador1Id = JugadorId1,
    jugador2Id = JugadorId2

)