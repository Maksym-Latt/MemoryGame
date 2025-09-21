package com.manacode.memorygame.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manacode.memorygame.data.local.entity.GameResult
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: GameResult): Long

    @Query("SELECT * FROM game_results ORDER BY timestamp DESC")
    fun getPagedResults(): PagingSource<Int, GameResult>
}