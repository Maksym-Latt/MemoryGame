package com.manacode.memorygame.ui

import app.cash.turbine.test
import com.manacode.memorygame.data.repo.GameRepository
import com.manacode.memorygame.data.util.Resource
import com.manacode.memorygame.model.GameOver
import com.manacode.memorygame.ui.game.GameUiState
import com.manacode.memorygame.ui.game.GameViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private lateinit var repository: GameRepository
    private lateinit var viewModel: GameViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // подменяем Main

        repository = mockk()
        coEvery { repository.saveResult(any()) } returns Resource.Success(1L)

        viewModel = GameViewModel(
            repository = repository,
            savedStateHandle = androidx.lifecycle.SavedStateHandle()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startGame initializes state`() = runTest {
        viewModel.startGame()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(0, state.pairsFound)
        assertNotNull(state.cards)
        assertTrue(state.cards.isNotEmpty())
    }

    @Test
    fun `onCardSelected flips a card`() = runTest {
        viewModel.startGame()
        advanceUntilIdle()

        val firstIndex = 0
        val before = viewModel.uiState.value
        assertFalse(before.cards[firstIndex].isFaceUp)

        viewModel.onCardSelected(firstIndex)
        advanceUntilIdle()

        val after = viewModel.uiState.value
        assertTrue(after.cards[firstIndex].isFaceUp)
    }

    @Test
    fun `game over triggers saveResult`() = runTest {
        viewModel.startGame()
        advanceUntilIdle()


        val saveResultMethod = GameViewModel::class.java.getDeclaredMethod(
            "saveResult",
            Boolean::class.java
        )
        saveResultMethod.isAccessible = true
        saveResultMethod.invoke(viewModel, true)


        advanceUntilIdle()

        val result = viewModel.saveResultState.value
        assertNotNull(result)
        assertTrue(result is Resource.Success)
        assertEquals(1L, (result as Resource.Success).data)
    }
}
