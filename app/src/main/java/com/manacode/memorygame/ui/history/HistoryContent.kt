package com.manacode.memorygame.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.manacode.memorygame.data.local.entity.GameResult
import com.manacode.memorygame.ui.history.component.*

@Composable
internal fun HistoryContent(
    lazyHistory: LazyPagingItems<GameResult>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HistoryStatistics(lazyHistory = lazyHistory)

        Spacer(modifier = Modifier.height(24.dp))

        when (lazyHistory.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingIndicator()
            }
            is LoadState.Error -> {
                ErrorState()
            }
            else -> {
                if (lazyHistory.itemCount == 0) {
                    EmptyState()
                } else {
                    HistoryList(lazyHistory = lazyHistory)
                }
            }
        }
    }
}
