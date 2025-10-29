package com.example.registrojugadoresjohanreinosoap2.data.mapper

import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerEntity
import com.example.registrojugadoresjohanreinosoap2.data.remote.dto.JugadorRequest
import com.example.registrojugadoresjohanreinosoap2.data.remote.dto.JugadorResponse
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import java.util.UUID

fun PlayerEntity.toDomain():Player=
    Player(
        Jugadorid = Jugadorid,
        Nombres = Nombres,
        Partidas = Partidas
    )

fun Player.toEntity():PlayerEntity=
    PlayerEntity(
        Jugadorid = Jugadorid,
        Nombres = Nombres,
        Partidas = Partidas
    )

fun JugadorResponse.toEntity(): PlayerEntity = PlayerEntity(
    Jugadorid = UUID.randomUUID().toString(),
    remoteId = jugadorId,
    Nombres = nombres,
    Partidas = 0
)

fun JugadorResponse.toDomain(): Player = Player(
    Jugadorid = UUID.randomUUID().toString(),
    remoteId = jugadorId,
    Nombres = nombres,
    Partidas = 0
)

fun PlayerEntity.toRequest(): JugadorRequest = JugadorRequest(
    nombres = this.Nombres,
    email = ""
)

fun Player.toRequest(): JugadorRequest = JugadorRequest(
    nombres = Nombres,
    email = ""
)