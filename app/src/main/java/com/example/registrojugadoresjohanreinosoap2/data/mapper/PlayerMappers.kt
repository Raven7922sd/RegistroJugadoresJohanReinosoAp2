package com.example.registrojugadoresjohanreinosoap2.data.mapper

import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerEntity
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player

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