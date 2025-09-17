package com.example.registrojugadoresjohanreinosoap2.data.module

import android.content.Context
import androidx.room.Room
import com.example.registrojugadoresjohanreinosoap2.data.db.PlayerDb
import com.example.registrojugadoresjohanreinosoap2.data.local.GameDao
import com.example.registrojugadoresjohanreinosoap2.data.local.PlayerDao
import com.example.registrojugadoresjohanreinosoap2.data.repository.GameRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.data.repository.PlayerRepositoryImpl
import com.example.registrojugadoresjohanreinosoap2.domain.repository.GameRepository
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

}