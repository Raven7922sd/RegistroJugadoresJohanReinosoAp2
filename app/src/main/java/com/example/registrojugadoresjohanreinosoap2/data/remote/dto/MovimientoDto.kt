package com.example.registrojugadoresjohanreinosoap2.data.remote.dto

data class MovimientoDto(
    val partidaId: Int? = null,
    val jugador: String,
    val posicionFila: Int,
    val posicionColumna: Int
)