package com.example.registrojugadoresjohanreinosoap2.data.repository

import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toDomain
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toEntity
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toRequest
import com.example.registrojugadoresjohanreinosoap2.data.remote.Resource
import com.example.registrojugadoresjohanreinosoap2.data.remote.remoteDataSource.JugadorRemoteDataSource
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao,
    private val remoteDataSource: JugadorRemoteDataSource
) : PlayerRepository {

    override fun observePlayer(): Flow<List<Player>> {
        return playerDao.observerAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    override suspend fun getPlayer(id: String): Player? {
        return playerDao.getById(id)?.toDomain()
    }

    override suspend fun createPlayerLocal(player: Player): String {
        val entity = player.toEntity().copy(isPendingCreate = true)
        playerDao.upsert(entity)
        return "Jugador creado localmente"
    }

    override suspend fun upsert(player: Player) {
        val remoteId = player.remoteId ?: throw Exception("No remoteId")
        val request = player.toRequest()
        remoteDataSource.updateJugador(remoteId, request)

        val entity = player.toEntity()
        playerDao.upsert(entity)
    }

    override suspend fun delete(id: String) {
        val player = playerDao.getById(id) ?: throw Exception("Jugador no encontrado")
        val remoteId = player.remoteId ?: throw Exception("No remoteId")

        remoteDataSource.deleteJugador(remoteId)
        playerDao.deleteById(id)
    }

    override suspend fun getPlayersByName(nombre: String): List<Player> {
        return playerDao.getPlayersByName(nombre).map { it.toDomain() }
    }

    override suspend fun getAllPlayers(): List<Player> {
        try {
            cargarJugadoresApi()
        } catch (e: Exception) {
        }
        return playerDao.getAllPlayers().map { it.toDomain() }
    }

    override suspend fun postPendingPlayers(): Boolean {
        val pending = playerDao.getPendingCreateJugadores()
        for (entity in pending) {
            when (val result = remoteDataSource.createJugador(entity.toRequest())) {
                is Resource.Success -> {
                    result.data?.let { response ->
                        val synced = entity.copy(remoteId = response.jugadorId, isPendingCreate = false)
                        playerDao.upsert(synced)
                    }
                }
                is Resource.Error -> return false
                is Resource.Loading -> return false
            }
        }
        return true
    }

    override suspend fun cargarJugadoresApi(): Boolean {
        return when (val result = remoteDataSource.getJugadores()) {
            is Resource.Success -> {
                result.data?.forEach { jugadorResponse ->
                    val existingEntity = if (jugadorResponse.jugadorId != null) {
                        playerDao.getByRemoteId(jugadorResponse.jugadorId)
                    } else null

                    val entity = if (existingEntity != null) {
                        existingEntity.copy(Nombres = jugadorResponse.nombres)
                    } else {
                        jugadorResponse.toEntity()
                    }
                    playerDao.upsert(entity)
                }
                true
            }
            is Resource.Error -> false
            is Resource.Loading -> false
        }
    }
}

