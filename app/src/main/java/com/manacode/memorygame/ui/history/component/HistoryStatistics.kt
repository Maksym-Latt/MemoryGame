package com.manacode.memorygame.ui.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.manacode.memorygame.data.local.entity.GameResult

@Composable
internal fun HistoryStatistics(
    lazyHistory: LazyPagingItems<GameResult>,
    modifier: Modifier = Modifier
) {
    val totalGames = remember(lazyHistory.itemCount) { lazyHistory.itemCount }
    val wins = remember(lazyHistory.itemSnapshotList) {
        lazyHistory.itemSnapshotList.count { it?.isWin == true }
    }
    val winRate = if (totalGames > 0) (wins * 100f / totalGames) else 0f

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(
                    value = totalGames.toString(),
                    label = "Total Games",
                    icon = Icons.Default.History
                )

                StatItem(
                    value = wins.toString(),
                    label = "Wins",
                    icon = Icons.Default.Info,
                    color = MaterialTheme.colorScheme.primary
                )

                StatItem(
                    value = "${winRate.toInt()}%",
                    label = "Win Rate",
                    icon = Icons.Default.Info,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}