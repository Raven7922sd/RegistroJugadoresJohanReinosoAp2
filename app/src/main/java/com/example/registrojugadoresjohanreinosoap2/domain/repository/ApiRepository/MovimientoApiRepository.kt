package com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository

import com.example.registrojugadoresjohanreinosoap2.domain.model.Movimiento.Movimientos

interface MovimientoApiRepository {
    suspend fun getMovimientos(partidaId: Int): List<Movimientos>
    suspend fun crearMovimiento(movimiento: Movimientos): Boolean
}