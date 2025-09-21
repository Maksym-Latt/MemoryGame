package com.manacode.memorygame.ui.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manacode.memorygame.model.GameCard
import com.manacode.memorygame.ui.game.MemoryCard

@Composable
internal fun GameGrid(
    cards: List<GameCard>,
    onCardSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        itemsIndexed(cards) { index, card ->
            MemoryCard(
                card = card,
                onClick = { onCardSelected(index) },
            )
        }
    }
}