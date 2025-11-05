package com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository

import com.example.registrojugadoresjohanreinosoap2.data.remote.Mapper.toDomain
import com.example.registrojugadoresjohanreinosoap2.data.remote.Mapper.toDto
import com.example.registrojugadoresjohanreinosoap2.data.remote.PartidaApi
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.PartidaApiRepository
import javax.inject.Inject

class ApiPartidaRepositoryImpl @Inject constructor(
    private val partidaApi: PartidaApi
) : PartidaApiRepository {

    override suspend fun getAllPartidas(): List<Game> {
        return try {
            partidaApi.getAllPartidas().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPartida(id: Int): Game? {
        return try {
            partidaApi.getPartida(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun crearPartida(partida: Game): Game? {
        return try {
            val dto = partida.toDto()
            val resultado = partidaApi.crearPartida(dto)
            resultado.toDomain()
        } catch (e: Exception) {
            null
        }
    }
}