
package com.example.registrojugadoresjohanreinosoap2.data.module

import android.content.Context
import androidx.room.Room
import com.example.registrojugadoresjohanreinosoap2.data.db.PlayerDb
import com.example.registrojugadoresjohanreinosoap2.data.local.GameDao
import com.example.registrojugadoresjohanreinosoap2.data.local.Logros.LogroDao
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.remote.JugadorApi
import com.example.registrojugadoresjohanreinosoap2.data.remote.MovimientosApi
import com.example.registrojugadoresjohanreinosoap2.data.remote.PartidaApi
import com.example.registrojugadoresjohanreinosoap2.data.remote.remoteDataSource.JugadorRemoteDataSource
import com.example.registrojugadoresjohanreinosoap2.data.repository.GameRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.PlayerRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository.ApiMovimientoRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository.ApiPartidaRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.logroRepository.LogroRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.MovimientoApiRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.PartidaApiRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(
    SingletonComponent::class)
@Module

object AppModule {
    @Provides
    @Singleton
    fun providePlayerDB(@ApplicationContext appContext: Context): PlayerDb {
        return Room.databaseBuilder(
            appContext,
            PlayerDb::class.java,
            "PlayerDb"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    private const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/"

    @Provides
    @Singleton
    fun providePlayerDao(PlayerDb: PlayerDb): PlayerDao {
        return PlayerDb.playerDao()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providePartidaApi(moshi: Moshi): PartidaApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PartidaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovimientoApi(moshi: Moshi): MovimientosApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MovimientosApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJugadorApi(moshi: Moshi): JugadorApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(JugadorApi::class.java)
    }

    @Provides
    @Singleton
    fun providePartidaApiRepository(api: PartidaApi): PartidaApiRepository {
        return ApiPartidaRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMovimientoApiRepository(api: MovimientosApi): MovimientoApiRepository {
        return ApiMovimientoRepositoryImpl(api)
    }
    @Provides
    @Singleton
    fun providePartidaDao(playerDB: PlayerDb): GameDao {
        return playerDB.GameDao()
    }

    @Provides
    @Singleton
    fun provideLogroDao(playerDB: PlayerDb): LogroDao {
        return playerDB.LogroDao()
    }

    @Provides
    @Singleton
    fun providePlayerRepositoryImpl(playerDao: PlayerDao, remoteDataSource: JugadorRemoteDataSource): PlayerRepository {
        return PlayerRepositoryImpl(playerDao, remoteDataSource)
    }

    @Provides
    @Singleton
    fun providePartidaRepositoryImpl(partidaDao: GameDao): GameRepositoryImpl {
        return GameRepositoryImpl(partidaDao)
    }

    @Provides
    @Singleton
    fun provideLogroRepositoryImpl(logroDao: LogroDao): LogroRepositoryImpl {
        return LogroRepositoryImpl(logroDao)
    }

    @Provides
    @Singleton
    fun providePartidaRepository(impl: GameRepositoryImpl): GameRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideLogroRepository(impl: LogroRepositoryImpl): LogroRepository {
        return impl
    }
}