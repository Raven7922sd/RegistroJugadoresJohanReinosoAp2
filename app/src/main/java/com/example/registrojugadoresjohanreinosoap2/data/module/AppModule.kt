package com.example.registrojugadoresjohanreinosoap2.data.module

import android.content.Context
import androidx.room.Room
import com.example.registrojugadoresjohanreinosoap2.data.db.PlayerDb
import com.example.registrojugadoresjohanreinosoap2.data.local.GameDao
import com.example.registrojugadoresjohanreinosoap2.data.local.Logros.LogroDao
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.JugadorApi
import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.MovimientosApi
import com.example.registrojugadoresjohanreinosoap2.data.remoteApi.PartidaApi
import com.example.registrojugadoresjohanreinosoap2.data.repository.GameRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.PlayerRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository.ApiJugadoresRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository.ApiMovimientoRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.apiRepository.ApiPartidaRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.logroRepository.LogroRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.domain.model.Movimiento.Movimientos
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.JugadorApiRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.MovimientoApiRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.ApiRepository.PartidaApiRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.LogrosRepository.LogroRepository
import com.example.registrojugadoresjohanreinosoap2.domain.repository.PlayerRepository
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.DeletePlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.GetPlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.ObservePlayersUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.UpsertPlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.ValidationPlayerUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.DeleteGameUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.GetGameUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.ObserveGameUseCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.UpsertGameCase
import com.example.registrojugadoresjohanreinosoap2.domain.usecase.gameUseCase.ValidationGameUseCase
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
    private const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/"

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
    fun provideJugadorApiRepository(api: JugadorApi): JugadorApiRepository {
        return ApiJugadoresRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMovimientoApiRepository(api: MovimientosApi): MovimientoApiRepository {
        return ApiMovimientoRepositoryImpl(api)
    }



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

    @Provides
    @Singleton
    fun providePlayerDao(playerDb: PlayerDb): PlayerDao {
        return playerDb.playerDao()
    }

    @Provides
    @Singleton
    fun providePlayerRepositoryImpl(playerDao: PlayerDao): PlayerRepositoryImpl {
        return PlayerRepositoryImpl(playerDao)
    }

    @Provides
    @Singleton
    fun providePlayerRepository(impl: PlayerRepositoryImpl): PlayerRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideGetPlayerUseCase(repo: PlayerRepository) = GetPlayerUseCase(repo)

    @Provides
    @Singleton
    fun provideUpsertPlayerUseCase(repo: PlayerRepository) = UpsertPlayerUseCase(repo)

    @Provides
    @Singleton
    fun provideDeletePlayerUseCase(repo: PlayerRepository) = DeletePlayerUseCase(repo)

    @Provides
    @Singleton
    fun provideObservePlayersUseCase(repo: PlayerRepository) = ObservePlayersUseCase(repo)

    @Provides
    @Singleton
    fun provideValidationPlayerUseCase(repo: PlayerRepository) = ValidationPlayerUseCase(repo)


    // Game Use Cases

    @Provides
    @Singleton
    fun provideUpsertGameUseCase(repo: GameRepository) = UpsertGameCase(repo)

    @Provides
    @Singleton
    fun provideObserveGameUseCase(repo: GameRepository) = ObserveGameUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteGameUseCase(repo: GameRepository) = DeleteGameUseCase(repo)

    @Provides
    @Singleton
    fun provideGetGameUseCase(repo: GameRepository) = GetGameUseCase(repo)

    @Provides
    @Singleton
    fun provideValidationGameUseCase(repo: GameRepository) = ValidationGameUseCase(repo)


    @Provides
    @Singleton
    fun provideGameRepository(impl: GameRepositoryImpl): GameRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideGameRepositoryImpl(gameDao: GameDao): GameRepositoryImpl {
        return GameRepositoryImpl(gameDao)
    }

    @Provides
    @Singleton
    fun provideGameDao(playerDb: PlayerDb): GameDao {
        return playerDb.GameDao()
    }


    //Logro Use Cases
    @Provides
    @Singleton
    fun provideLogroDao(playerDb: PlayerDb): LogroDao {
        return playerDb.LogroDao()
    }

    @Provides
    @Singleton
    fun provideLogroRepositoryImpl(logroDao: LogroDao): LogroRepositoryImpl {
        return LogroRepositoryImpl(logroDao)
    }

    @Provides
    @Singleton
    fun provideLogroRepository(impl: LogroRepositoryImpl): LogroRepository {
        return impl
    }
}