package com.example.registrojugadoresjohanreinosoap2.data.remote;

import com.example.registrojugadoresjohanreinosoap2.data.remote.dto.MovimientoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MovimientosApi {
    @GET("api/Movimientos/{partidaId}")
    suspend fun getMovimientos(@Path("partidaId") partidaId: Int): List<MovimientoDto>

    @POST("api/Movimientos")
    suspend fun crearMovimiento(@Body movimiento: MovimientoDto)
}