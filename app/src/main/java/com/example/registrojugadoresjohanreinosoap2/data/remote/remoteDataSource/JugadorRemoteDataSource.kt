package com.example.registrojugadoresjohanreinosoap2.data.remote.remoteDataSource


import com.example.registrojugadoresjohanreinosoap2.data.remote.JugadorApi
import com.example.registrojugadoresjohanreinosoap2.data.remote.Resource
import com.example.registrojugadoresjohanreinosoap2.data.remote.dto.JugadorRequest
import com.example.registrojugadoresjohanreinosoap2.data.remote.dto.JugadorResponse
import javax.inject.Inject

class JugadorRemoteDataSource @Inject constructor(
    private val api: JugadorApi
) {
    suspend fun getJugadores(): Resource<List<JugadorResponse>> {
        return try {
            val response = api.getAllJugadores()
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun createJugador(request: JugadorRequest): Resource<JugadorResponse> {
        return try {
            val response = api.crearJugador(request)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun updateJugador(id: Int, request: JugadorRequest): Resource<Unit> {
        return try {
            val response = api.updateJugador(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun deleteJugador(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteJugador(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
}