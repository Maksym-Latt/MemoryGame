package com.manacode.memorygame.config

data class GameConfig(
    val totalLives: Int,
    val totalPairs: Int,
    val flipDelayMillis: Long,
    val previewMillis: Long
)