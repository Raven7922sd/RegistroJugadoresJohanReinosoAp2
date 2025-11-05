package com.example.registrojugadoresjohanreinosoap2

import android.app.Application
import androidx.work.Configuration
import com.example.registrojugadoresjohanreinosoap2.data.workers.MyWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RegistroJugadores : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: MyWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}