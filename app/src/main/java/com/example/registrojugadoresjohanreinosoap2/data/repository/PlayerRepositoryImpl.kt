package com.example.registrojugadoresjohanreinosoap2.data.repository

import androidx.room.Delete
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toDomain
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toEntity
import com.example.registrojugadoresjohanreinosoap2.domain.model.Player
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import java.util.Collections.list

class PlayerRepositoryImpl(
    private val dao: PlayerDao
): PlayerRepository {

    override fun observePlayer(): Flow<List<Player>> =
        dao.observerAll().map { list ->
            list.map { it.toDomain() } }

    override suspend fun getPlayer(id: Int): Player?=dao.getById(id)?.toDomain()

    override suspend fun upsert(player: Player): Int {
        dao.upsert(player.toEntity())
        return player.Jugadorid
    }

    override suspend fun delete(id: Int){
        dao.deleteById(id)
    }
}