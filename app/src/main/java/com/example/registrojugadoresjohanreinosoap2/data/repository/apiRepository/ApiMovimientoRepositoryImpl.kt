package com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository

import com.example.registrojugadoresjohanreinosoap2.data.remote.Mapper.toDomain
import com.example.registrojugadoresjohanreinosoap2.data.remote.Mapper.toDto
import com.example.registrojugadoresjohanreinosoap2.data.remote.MovimientosApi
import com.example.registrojugadoresjohanreinosoap2.domain.model.Movimiento.Movimientos
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.MovimientoApiRepository
import javax.inject.Inject

class ApiMovimientoRepositoryImpl @Inject constructor(
    private val MovimientosApi: MovimientosApi
) : MovimientoApiRepository {

    override suspend fun getMovimientos(partidaId: Int): List<Movimientos> {
        return try {
            MovimientosApi.getMovimientos(partidaId).map { it.toDomain(partidaId) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun crearMovimiento(movimiento: Movimientos): Boolean {
        return try {
            val dto = movimiento.toDto()
            MovimientosApi.crearMovimiento(dto)
            true
        } catch (e: Exception) {
            false
        }
    }
}