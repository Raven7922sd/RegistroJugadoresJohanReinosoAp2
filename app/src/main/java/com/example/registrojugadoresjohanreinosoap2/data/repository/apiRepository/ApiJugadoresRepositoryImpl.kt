package com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository

import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.JugadorApi
import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.Mapper.toDomain
import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.Mapper.toDto
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.JugadorApiRepository
import javax.inject.Inject

class ApiJugadoresRepositoryImpl @Inject constructor(
    private val jugadorApi: JugadorApi
) : JugadorApiRepository {
    override suspend fun getAllJugadores(): List<Player> {
        return try {
            jugadorApi.getAllJugadores().map { it.toDomain() }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getJugador(id: Int): Player? {
        return try {
            jugadorApi.getJugador(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun crearJugador(jugador: Player): Player? {
        return try {
            val jugadorDto = jugador.toDto()
            jugadorApi.crearJugador(jugadorDto).toDomain()
        } catch (e: Exception) {
            null
        }
    }
}