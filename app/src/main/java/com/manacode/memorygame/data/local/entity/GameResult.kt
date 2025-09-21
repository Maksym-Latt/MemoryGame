package com.manacode.memorygame.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pairsFound: Int,
    val totalPairs: Int,
    val isWin: Boolean,
    val timestamp: Long
)