package com.example.registrojugadoresjohanreinosoap2.data.mapper

import com.example.registrojugadoresjohanreinosoap2.data.local.GameEntity
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game

fun GameEntity.toDomain(): Game =
    Game(
        Gameid = Gameid,
        Fecha = Fecha,
        JugadorId1 = JugadorId1,
        JugadorId2 = JugadorId2,
        GanadorId = GanadorId,
        EsFinalizada
    )
fun Game.toEntity(): GameEntity =
    GameEntity(
        Gameid = Gameid,
        Fecha = Fecha,
        JugadorId1 = JugadorId1,
        JugadorId2 = JugadorId2,
        GanadorId = GanadorId,
        EsFinalizada = EsFinalizada
    )