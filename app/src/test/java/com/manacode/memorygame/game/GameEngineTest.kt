package com.manacode.memorygame.game

import com.manacode.memorygame.config.GameConfig
import com.manacode.memorygame.engine.GameEngine
import com.manacode.memorygame.model.GameCard
import com.manacode.memorygame.model.GameOver
import com.manacode.memorygame.ui.game.GameUiState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var engine: GameEngine
    private lateinit var initialState: GameUiState

    @Before
    fun setup() {
        val config = GameConfig(totalLives = 3, totalPairs = 2, previewMillis = 1000, flipDelayMillis = 500)
        engine = GameEngine(config)

        // Стартовое состояние с двумя парами
        val cards = listOf(
            GameCard(id = 1, pairId = 1, imageRes = 101),
            GameCard(id = 2, pairId = 1, imageRes = 101),
            GameCard(id = 3, pairId = 2, imageRes = 102),
            GameCard(id = 4, pairId = 2, imageRes = 102),
        )
        initialState = GameUiState(
            cards = cards,
            lives = config.totalLives,
            pairsFound = 0,
            gameOver = null
        )
    }

    @Test
    fun `flipCard should flip a facedown card`() {
        val newState = engine.flipCard(initialState, 0)
        assertTrue(newState.cards[0].isFaceUp)
    }

    @Test
    fun `flipCard should ignore already faceUp card`() {
        val flippedOnce = engine.flipCard(initialState, 0)
        val flippedTwice = engine.flipCard(flippedOnce, 0)
        assertTrue(flippedTwice.cards[0].isFaceUp)
    }

    @Test
    fun `handlePair should match correct pair`() {
        val flipped1 = engine.flipCard(initialState, 0)
        val flipped2 = engine.flipCard(flipped1, 1)

        val result = engine.handlePair(flipped2, 0, 1)
        assertTrue(result.matched)
        assertTrue(result.newState.cards[0].isMatched)
        assertEquals(1, result.newState.pairsFound)
    }

    @Test
    fun `handlePair should decrease lives on mismatch`() {
        val flipped1 = engine.flipCard(initialState, 0)
        val flipped2 = engine.flipCard(flipped1, 2)

        val result = engine.handlePair(flipped2, 0, 2)
        assertFalse(result.matched)
        assertEquals(2, result.newState.lives)
    }

    @Test
    fun `checkGameOver should detect win`() {
        val state = initialState.copy(pairsFound = 2)
        val gameOver = engine.checkGameOver(state)
        assertEquals(GameOver.Win, gameOver)
    }

    @Test
    fun `checkGameOver should detect lose`() {
        val state = initialState.copy(lives = 0)
        val gameOver = engine.checkGameOver(state)
        assertEquals(GameOver.Lose, gameOver)
    }
}