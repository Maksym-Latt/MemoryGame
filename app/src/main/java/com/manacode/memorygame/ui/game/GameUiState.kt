package com.manacode.memorygame.ui.game

import android.os.Parcelable
import com.manacode.memorygame.model.GameCard
import com.manacode.memorygame.model.GameOver
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameUiState(
    val cards: List<GameCard> = emptyList(),
    val lives: Int = 5,
    val pairsFound: Int = 0,
    val gameOver: GameOver? = null,
    val isLoading: Boolean = false
) : Parcelable