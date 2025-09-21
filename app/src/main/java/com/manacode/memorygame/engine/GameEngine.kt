package com.manacode.memorygame.engine

import com.manacode.memorygame.config.GameConfig
import com.manacode.memorygame.model.GameCard
import com.manacode.memorygame.model.GameOver
import com.manacode.memorygame.ui.game.GameUiState

data class FlipResult(
    val newState: GameUiState,
    val matched: Boolean
)

/**
 * Core game logic engine for the memory game.
 * Responsible for flipping cards, handling matches, and determining game over conditions.
 * Follows Single Responsibility Principle: only contains pure game logic, no UI code.
 */
class GameEngine(private val config: GameConfig) {

    /**
     * Checks whether a card can be flipped.
     * Cards cannot be flipped if the game is over.
     *
     * @param state current game UI state
     * @return true if flipping is allowed, false otherwise
     */
    fun canFlip(state: GameUiState): Boolean =
        state.gameOver == null

    /**
     * Flips a single card.
     * Returns a new GameUiState with the updated card state.
     * Does nothing if the card is already face up, matched, or the game is over.
     *
     * @param state current game UI state
     * @param index index of the card to flip
     * @return new GameUiState with updated card
     */
    fun flipCard(state: GameUiState, index: Int): GameUiState {
        if (!canFlip(state)) return state

        val card = state.cards.getOrNull(index) ?: return state
        if (card.isFaceUp || card.isMatched) return state

        // Create a copy of cards list to maintain immutability
        val newCards = state.cards.toMutableList()
        newCards[index] = card.copy(isFaceUp = true)

        return state.copy(cards = newCards)
    }

    /**
     * Handles a pair of selected cards.
     * If the cards match, marks them as matched and increments pairsFound.
     * If they don't match, flips them back and decrements lives (cannot go below 0).
     *
     * @param state current game UI state
     * @param firstIndex index of the first selected card
     * @param secondIndex index of the second selected card
     * @return FlipResult containing new state and whether the pair matched
     */
    fun handlePair(state: GameUiState, firstIndex: Int, secondIndex: Int): FlipResult {
        val firstCard = state.cards[firstIndex]
        val secondCard = state.cards[secondIndex]

        return if (firstCard.pairId == secondCard.pairId) {
            // Pair matched: mark both cards as matched and increment pairsFound
            val newCards = state.cards.mapIndexed { i, card ->
                if (i == firstIndex || i == secondIndex) card.copy(isFaceUp = true, isMatched = true)
                else card
            }
            FlipResult(
                newState = state.copy(
                    cards = newCards,
                    pairsFound = state.pairsFound + 1
                ),
                matched = true
            )
        } else {
            // Pair did not match: flip cards back and decrement lives safely
            val newLives = (state.lives - 1).coerceAtLeast(0)
            val newCards = state.cards.mapIndexed { i, card ->
                if (i == firstIndex || i == secondIndex) card.copy(isFaceUp = false)
                else card
            }
            FlipResult(
                newState = state.copy(
                    cards = newCards,
                    lives = newLives
                ),
                matched = false
            )
        }
    }

    /**
     * Checks whether the game is over.
     * Game is won if all pairs are found.
     * Game is lost if lives reach zero.
     *
     * @param state current game UI state
     * @return GameOver.Win, GameOver.Lose, or null if the game continues
     */
    fun checkGameOver(state: GameUiState): GameOver? = when {
        state.pairsFound >= config.totalPairs -> GameOver.Win
        state.lives <= 0 -> GameOver.Lose
        else -> null
    }
}
