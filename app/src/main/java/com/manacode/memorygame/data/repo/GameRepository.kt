package com.manacode.memorygame.data.repo

import androidx.paging.PagingData
import com.manacode.memorygame.data.local.entity.GameResult
import com.manacode.memorygame.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getHistory(): Flow<PagingData<GameResult>>
    suspend fun saveResult(result: GameResult): Resource<Long>
}