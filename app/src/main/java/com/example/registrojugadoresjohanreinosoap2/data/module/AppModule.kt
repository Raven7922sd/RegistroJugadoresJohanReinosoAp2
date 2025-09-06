package com.example.registrojugadoresjohanreinosoap2.data.module

import android.content.Context
import androidx.room.Room
import com.example.registrojugadoresjohanreinosoap2.data.db.PlayerDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(
    SingletonComponent::class)
@Module

object AppModule {
    @Provides
    @Singleton
    fun providePlayer(@ApplicationContext appContext: Context)=
        Room.databaseBuilder(
                appContext,
                PlayerDb::class.java,
                "player_db"
            ).fallbackToDestructiveMigration(false)
            .build()
}