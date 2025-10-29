package com.example.registrojugadoresjohanreinosoap2.data.remoteApi.Mapper

import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.dto.MovimientoDto
import com.example.registrojugadoresjohanreinosoap2.domain.model.Movimiento.Movimientos

fun MovimientoDto.toDomain(partidaId: Int): Movimientos = Movimientos(
    partidaId = partidaId,
    jugador = jugador,
    posicionFila = posicionFila,
    posicionColumna = posicionColumna,
)

fun Movimientos.toDto(): MovimientoDto = MovimientoDto(
    partidaId = partidaId,
    jugador = jugador,
    posicionFila = posicionFila,
    posicionColumna = posicionColumna
)