package com.example.registrojugadoresjohanreinosoap2.data.repository

import com.example.registrojugadoresjohanreinosoap2.data.local.GameDao
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toDomain
import com.example.registrojugadoresjohanreinosoap2.data.mapper.toEntity
import com.example.registrojugadoresjohanreinosoap2.domain.model.Game
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepositoryImpl @Inject constructor(
    private val gameDao: GameDao
): GameRepository {

    override fun observeGame(): Flow<List<Game>>{
        return gameDao.observerAll().map {entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getGame(id: Int): Game? {
        return gameDao.getById(id)?.toDomain()
    }
    override suspend fun upsert(game: Game): Int {
        val entity = game.toEntity()
        val result = gameDao.upsert(entity)
        return if (game.Gameid == 0) result.toInt() else game.Gameid
    }

    override suspend fun delete(id: Int) {
        gameDao.deleteById(id)
    }

    override suspend fun getAllGames(): List<Game> {
        return gameDao.getAllGames().map { it.toDomain() }
    }

    override fun getPartidasEnCurso(): Flow<List<Game>> {
        return gameDao.getPartidasEnCurso()
    }
}