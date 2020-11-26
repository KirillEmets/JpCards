package com.kirillemets.flashcards.review

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.roundToInt

class ReviewFragmentViewModelTest {
    val hard_mult = 1.3
    val easy_mult = 1.8
    @Test
    fun getNewDelay1_easy() {
        val lastDelay = 1
        val difficulty = 1
        val newDelay = ReviewFragmentViewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * easy_mult).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay1_hard() {
        val lastDelay = 1
        val difficulty = 2
        val newDelay = ReviewFragmentViewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * hard_mult).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay12_easy() {
        val lastDelay = 12
        val difficulty = 1
        val newDelay = ReviewFragmentViewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * easy_mult).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay12_hard() {
        val lastDelay = 12
        val difficulty = 2
        val newDelay = ReviewFragmentViewModel.getNewDelay(lastDelay, difficulty)
        val expected = (lastDelay * hard_mult).roundToInt()
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay1_other() {
        val lastDelay = 1
        val difficulty = -1
        val newDelay = ReviewFragmentViewModel.getNewDelay(lastDelay, difficulty)
        val expected = 1
        assertEquals(expected, newDelay)
    }

    @Test
    fun getNewDelay12_other() {
        val lastDelay = 12
        val difficulty = -1
        val newDelay = ReviewFragmentViewModel.getNewDelay(lastDelay, difficulty)
        val expected = 12
        assertEquals(expected, newDelay)
    }


}