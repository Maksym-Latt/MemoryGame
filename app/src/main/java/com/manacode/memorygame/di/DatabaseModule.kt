package com.manacode.memorygame.di

import android.content.Context
import androidx.room.Room
import com.manacode.memorygame.data.repo.GameRepository
import com.manacode.memorygame.data.local.AppDatabase
import com.manacode.memorygame.data.local.dao.GameResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "memory_game_db"
        ).build()

    @Provides
    fun provideGameResultDao(db: AppDatabase): GameResultDao = db.gameResultDao()
}