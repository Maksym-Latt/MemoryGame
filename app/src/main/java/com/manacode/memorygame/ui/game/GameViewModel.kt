package com.manacode.memorygame.ui.game

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manacode.memorygame.config.GameDifficulty
import com.manacode.memorygame.data.repo.GameRepository
import com.manacode.memorygame.data.local.entity.GameResult
import com.manacode.memorygame.data.util.Resource
import com.manacode.memorygame.engine.GameEngine
import com.manacode.memorygame.model.GameOver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val STATE_KEY = "game_state"
    }

    private val config = GameDifficulty.MEDIUM.config
    private val engine = GameEngine(config)

    // StateFlow holding the current UI state
    private val _uiState = MutableStateFlow(
        savedStateHandle.get<GameUiState>(STATE_KEY) ?: GameUiState()
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // StateFlow for save result operation status
    private val _saveResultState = MutableStateFlow<Resource<Long>?>(null)
    val saveResultState: StateFlow<Resource<Long>?> = _saveResultState.asStateFlow()

    private var firstSelectedIndex: Int? = null
    private var isProcessing: Boolean = false

    init {
        if (_uiState.value.cards.isEmpty()) startGame()
    }

    /**
     * Starts a new game.
     * 1. Generates cards and shows them for a short preview.
     * 2. Resets lives, pairsFound, and game over state.
     * 3. After previewMillis, flips all cards face down and unlocks UI.
     */
    fun startGame() {
        val newCards = GameCardGenerator.generateCards()

        updateState(
            GameUiState(
                cards = newCards.map { it.copy(isFaceUp = true) },
                lives = config.totalLives,
                pairsFound = 0,
                gameOver = null,
                isLoading = true
            )
        )

        firstSelectedIndex = null
        isProcessing = false

        // Launch a coroutine for preview delay
        viewModelScope.launch {
            delay(config.previewMillis)
            updateState(_uiState.value.copy(
                cards = _uiState.value.cards.map { it.copy(isFaceUp = false) },
                isLoading = false
            ))
        }
    }

    /**
     * Handles user card selection.
     * Prevents flipping if another flip is in progress or the game is over.
     * Uses GameEngine for card flip and pair handling.
     */
    fun onCardSelected(index: Int) {
        val currentState = _uiState.value
        if (!engine.canFlip(currentState) || isProcessing) return

        // Flip the selected card
        val flippedState = engine.flipCard(currentState, index)
        updateState(flippedState)

        val firstIndex = firstSelectedIndex
        if (firstIndex == null) {
            // First card selected in this turn
            firstSelectedIndex = index
            return
        }

        // Handle second card selection in a separate coroutine
        isProcessing = true
        viewModelScope.launch {
            val result = engine.handlePair(_uiState.value, firstIndex, index)

            if (!result.matched) delay(config.flipDelayMillis)

            updateState(result.newState)

            val gameOver = engine.checkGameOver(result.newState)
            if (gameOver != null) {
                updateState(result.newState.copy(gameOver = gameOver))
                saveResult(gameOver == GameOver.Win)
            }

            firstSelectedIndex = null
            isProcessing = false
        }
    }

    /**
     * Saves the game result to the repository.
     * Updates _saveResultState with success or error.
     */
    private fun saveResult(isWin: Boolean) {
        val state = _uiState.value
        viewModelScope.launch {
            val result = runCatching {
                repository.saveResult(
                    GameResult(
                        pairsFound = state.pairsFound,
                        totalPairs = config.totalPairs,
                        isWin = isWin,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }.getOrElse { e ->
                Resource.Error("Failed to save result", e)
            }

            _saveResultState.value = result

            if (result is Resource.Error) {
                Log.e("GameViewModel", "Failed to save game result", result.throwable)
            }
        }
    }

    private fun updateState(newState: GameUiState) {
        _uiState.value = newState
        savedStateHandle[STATE_KEY] = newState
    }


    fun setTestState(state: GameUiState) {
        updateState(state)
    }
}