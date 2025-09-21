package com.manacode.memorygame.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.manacode.memorygame.data.repo.GameRepository
import com.manacode.memorygame.data.local.entity.GameResult
import com.manacode.memorygame.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    val history: Flow<PagingData<GameResult>> = repository.getHistory()
}