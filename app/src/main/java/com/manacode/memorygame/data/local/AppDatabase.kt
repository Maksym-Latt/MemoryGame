package com.manacode.memorygame.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.manacode.memorygame.data.local.dao.GameResultDao
import com.manacode.memorygame.data.local.entity.GameResult


@Database(
    entities = [GameResult::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
}