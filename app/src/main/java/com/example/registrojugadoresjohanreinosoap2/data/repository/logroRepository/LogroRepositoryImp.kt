package com.example.registrojugadoresjohanreinosoap2.data.repository.logroRepository

import com.example.registrojugadoresjohanreinosoap2.data.local.Logros.LogroDao
import com.example.registrojugadoresjohanreinosoap2.data.mapper.LogroMappers.toEntity
import com.example.registrojugadoresjohanreinosoap2.data.mapper.LogroMappers.toLogro
import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros
import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogroRepositoryImpl @Inject constructor(
    private val LogroDao: LogroDao
) : LogroRepository {

    override fun observeLogro(): Flow<List<Logros>> {
        return LogroDao.observerAll().map { entities ->
            entities.map { it.toLogro() }
        }
    }

    override suspend fun getLogro(id: Int): Logros? {
        return LogroDao.getById(id)?.toLogro()
    }

    override suspend fun upsert(logro: Logros): Int {
        val entity = logro.toEntity()
        val result = LogroDao.upsert(entity)
        return if (logro.LogroId == 0) result.toInt() else logro.LogroId
    }

    override suspend fun delete(id: Int) {
        LogroDao.deleteById(id)
    }
}