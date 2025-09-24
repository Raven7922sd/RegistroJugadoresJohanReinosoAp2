package com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository

import com.example.registrojugadoresjohanreinosoap2.domain.model.Logros.Logros
import kotlinx.coroutines.flow.Flow

interface LogroRepository {

        fun observeLogro(): Flow<List<Logros>>

        suspend fun getLogro(id:Int): Logros?

        suspend fun upsert(logro: Logros):Int

        suspend fun delete(id:Int)
}