package com.example.registrojugadoresjohanreinosoap2.data.repository

import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerEntity
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toDomain
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toEntity
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao
) : PlayerRepository {

    override fun observePlayer(): Flow<List<Player>> {
        return playerDao.observerAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    override suspend fun getPlayer(id: Int): Player? {
        return playerDao.getById(id)?.toDomain()
    }
    override suspend fun upsert(player: Player): Int {
        val entity = player.toEntity()
        val result = playerDao.upsert(entity)
        return if (player.Jugadorid == 0) result.toInt() else player.Jugadorid
    }

    override suspend fun delete(id: Int) {
        playerDao.deleteById(id)
    }

    override suspend fun getAllPlayers(): List<Player> {
        return playerDao.getAllPlayers().map { it.toDomain() }
    }
}