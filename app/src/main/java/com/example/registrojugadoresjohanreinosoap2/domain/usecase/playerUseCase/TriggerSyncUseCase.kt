package com.example.registrojugadoresjohanreinosoap2.domain.usecase.playerUseCase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.registrojugadoresjohanreinosoap2.data.workers.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TriggerSyncUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}