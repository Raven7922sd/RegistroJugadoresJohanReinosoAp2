package com.example.registrojugadoresjohanreinosoap2.data.remote;

import com.example.registrojugadoresjohanreinosoap2.data.remote.dto.JugadorRequest
import com.example.registrojugadoresjohanreinosoap2.data.remote.dto.JugadorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JugadorApi {
    @GET("api/Jugadores")
    suspend fun getAllJugadores(): List<JugadorResponse>

    @GET("api/Jugadores/{id}")
    suspend fun getJugador(@Path("id") id: Int): JugadorResponse

    @POST("api/Jugadores")
    suspend fun crearJugador(@Body jugador: JugadorRequest): JugadorResponse

    @PUT("api/Jugadores/{id}")
    suspend fun updateJugador(@Path("id") id: Int, @Body jugador: JugadorRequest): Response<Unit>

    @DELETE("api/Jugadores/{id}")
    suspend fun deleteJugador(@Path("id") id: Int): Response<Unit>
}