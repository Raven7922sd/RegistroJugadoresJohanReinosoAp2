package com.example.registrojugadoresjohanreinosoap2.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val playerRepository: PlayerRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val downloadSuccess = playerRepository.cargarJugadoresApi()
            val success = playerRepository.postPendingPlayers()
            val overallSuccess = downloadSuccess || success
            if (overallSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
}