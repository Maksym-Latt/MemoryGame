package com.manacode.memorygame.config


enum class GameDifficulty(val config: GameConfig) {
    MEDIUM(
        GameConfig(
            totalLives = 5,
            totalPairs = 6,
            flipDelayMillis = 1000,
            previewMillis = 2000
        )
    )
}