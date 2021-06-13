package com.kirillemets.flashcards.addWord

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kirillemets.flashcards.MockDaoTest
import com.kirillemets.flashcards.database.DatabaseRepository
import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AddWordFragmentViewModelTest: MockDaoTest() {
    private lateinit var viewModel: AddWordFragmentViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun before() {
        mockDatabase()
        viewModel = AddWordFragmentViewModel(DatabaseRepository(dao))
    }

    @Test
    fun addWordButtonClicked() = runBlocking {
        val card = SearchResultCard("japan", "reading", listOf("eng1", "eng2"))

        var d: CompletableDeferred<Boolean> = CompletableDeferred()
        viewModel.insertionResult.observeForever {
            d.complete(it)
        }

        viewModel.onAddButtonClicked(card, 0)
        assertEquals(true, d.await())
        d = CompletableDeferred()

        assertEquals(1, dao.getAllBlocking().size)

        viewModel.onAddButtonClicked(card, 1)
        assertEquals(true, d.await())
        d = CompletableDeferred()

        viewModel.onAddButtonClicked(card, 0)
        assertEquals(false, d.await())
        d = CompletableDeferred()

        assertEquals(2, dao.getAllBlocking().size)
    }
}