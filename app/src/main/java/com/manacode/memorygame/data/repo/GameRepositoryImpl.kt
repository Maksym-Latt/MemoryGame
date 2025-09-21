package com.manacode.memorygame.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.manacode.memorygame.data.local.dao.GameResultDao
import com.manacode.memorygame.data.local.entity.GameResult
import com.manacode.memorygame.data.util.Resource
import com.manacode.memorygame.data.repo.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val dao: GameResultDao
) : GameRepository {

    override fun getHistory(): Flow<PagingData<GameResult>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getPagedResults() }
        ).flow

    override suspend fun saveResult(result: GameResult): Resource<Long> {
        return runCatching {
            val id = dao.insert(result)
            Resource.Success(id)
        }.getOrElse { e ->
            Resource.Error("Failed to save result", e)
        }
    }
}
