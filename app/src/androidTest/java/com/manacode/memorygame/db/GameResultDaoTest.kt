package com.manacode.memorygame.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.manacode.memorygame.data.local.dao.GameResultDao
import com.manacode.memorygame.data.local.entity.GameResult
import com.manacode.memorygame.data.local.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameResultDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: GameResultDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.gameResultDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndRetrieveGameResult() = runBlocking {
        val result = GameResult(
            pairsFound = 5,
            totalPairs = 6,
            isWin = true,
            timestamp = System.currentTimeMillis()
        )
        val id = dao.insert(result)

        val results = dao.getPagedResults().load(
            androidx.paging.PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ).run {
            requireNotNull(this)
        }

        assertTrue(results is androidx.paging.PagingSource.LoadResult.Page)
    }

}
