package com.example.registrojugadoresjohanreinosoap2.data.remoteApi;

import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.dto.JugadorDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JugadorApi {
    @GET("api/Jugadores")
    suspend fun getAllJugadores(): List<JugadorDto>

    @GET("api/Jugadores/{id}")
    suspend fun getJugador(@Path("id") id: Int): JugadorDto

    @POST("api/Jugadores")
    suspend fun crearJugador(@Body jugador: JugadorDto): JugadorDto
}