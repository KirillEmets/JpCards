package com.kirillemets.flashcards.addWord

import android.util.Log
import com.kirillemets.flashcards.database.FlashCardRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AddWordFragmentViewModelTest {
    private lateinit var viewModel: AddWordFragmentViewModel

    @get:Rule
    var rule = HiltAndroidRule(this)

    @Inject lateinit var repository: FlashCardRepository

    @Before
    fun init() {
        rule.inject()
        viewModel = AddWordFragmentViewModel(repository)
    }

    @Test
    fun addWordButtonClicked() = runBlocking(Dispatchers.Main) {
        val card = SearchResultCard("japan", "reading", listOf("eng1", "eng2"))

        val cb = iterableCallback<Boolean> {
            viewModel.insertionResult.observeForever {
                emit(it)
            }
        }

        viewModel.onAddButtonClicked(card, 0)
        assertEquals(true, cb.awaitNext())
//        repository.getAllSuspend()
        assertEquals(1, repository.getAllSuspend().size)


        viewModel.onAddButtonClicked(card, 1)
        assertEquals(true, cb.awaitNext())


        viewModel.onAddButtonClicked(card, 0)
        assertEquals(false, cb.awaitNext())

//        repository.getAllSuspend()
        assertEquals(2, repository.getAllSuspend().size)
    }
}

class IterableCallback<T> {
    private lateinit var d: CompletableDeferred<T>
    fun putValue(value: T) {
        d.complete(value)
    }

    suspend fun awaitNext(): T {
        d = CompletableDeferred()
        return d.await()
    }
}

class IterableCallbackScope<T>(private val cb: IterableCallback<T>) {
    fun emit(value: T) {
        cb.putValue(value)
    }
}

fun<T> iterableCallback(body: IterableCallbackScope<T>.() -> Unit): IterableCallback<T> {
    val cb = IterableCallback<T>()
    val scope = IterableCallbackScope(cb)
    body(scope)
    return cb
}