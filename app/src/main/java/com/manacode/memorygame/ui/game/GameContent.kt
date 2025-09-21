package com.manacode.memorygame.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manacode.memorygame.model.GameOver
import com.manacode.memorygame.ui.game.component.*


@Composable
internal fun GameContent(
    uiState: GameUiState,
    onCardSelected: (Int) -> Unit,
    onRestart: () -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameStats(
            lives = uiState.lives,
            pairsFound = uiState.pairsFound,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GameGrid(
                cards = uiState.cards,
                onCardSelected = onCardSelected,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (uiState.isLoading) {
            PreviewTimerIndicator()
        }
        Spacer (modifier = Modifier.height(16.dp))
        uiState.gameOver?.let { gameOver ->
            GameOverMessage(
                isWin = gameOver is GameOver.Win,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        ActionButtons (
                onRestart = onRestart,
        onNavigateToHistory = onNavigateToHistory,
        modifier = Modifier.fillMaxWidth()
        )
    }
}
