package com.kirillemets.flashcards.review

import com.kirillemets.flashcards.database.DatabaseRepository
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.math.roundToInt

class ReviewFragmentViewModelTest {
    private val hardMultiplier = 1.3f
    private val easyMultiplier = 1.8f
    private val db = mock(DatabaseRepository::class.java)
    private val viewModel = ReviewFragmentViewModel(db).apply {
        delayEasyMultiplier = easyMultiplier
        delayHardMultiplier = hardMultiplier
    }

    @Test
    fun getNewDelay1_easy() {
        val lastDelay = 1
        val difficulty = 1
        val newDelay = viewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * easyMultiplier).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay1_hard() {
        val lastDelay = 1
        val difficulty = 2
        val newDelay = viewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * hardMultiplier).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay12_easy() {
        val lastDelay = 12
        val difficulty = 1
        val newDelay = viewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * easyMultiplier).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay12_hard() {
        val lastDelay = 12
        val difficulty = 2
        val newDelay = viewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * hardMultiplier).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay1_other() {
        val lastDelay = 1
        val difficulty = -1
        val newDelay = viewModel.getNewDelay(lastDelay, difficulty)
        val expected = 1
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay12_other() {
        val lastDelay = 12
        val difficulty = -1
        val newDelay = viewModel.getNewDelay(lastDelay, difficulty)
        val expected = 12
        assertEquals(expected, newDelay)
    }
}