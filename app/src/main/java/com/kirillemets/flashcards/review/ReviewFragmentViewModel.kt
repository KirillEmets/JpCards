package com.kirillemets.flashcards.review

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.*
import java.util.*

class ReviewFragmentViewModel(val database: CardDatabaseDao): ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    val reviewCards: MutableLiveData<List<ReviewCard>> = MutableLiveData(listOf())

    val buttonReviewClicked = MutableLiveData(false)

    init {
        makeCardsToReview()
    }

    private fun makeCardsToReview() {
        coroutineScope.launch() {
            val cards = getRelevantCardsFromDatabaseAsync().await()
            Log.i("HELLO", cards.size.toString())
            val newList = mutableListOf<ReviewCard>()
            cards.forEach { card ->
                newList.add(
                    ReviewCard(
                        card.japanese,
                        card.reading,
                        card.english,
                        false,
                        card.cardId
                    )
                )
                newList.add(
                    ReviewCard(
                        card.japanese,
                        card.reading,
                        card.english,
                        true,
                        card.cardId
                    )
                )
            }
            reviewCards.value = newList
        }
    }

    private fun getRelevantCardsFromDatabaseAsync(): Deferred<List<FlashCard>> =
        coroutineScope.async(Dispatchers.IO) { database.getRelevantCards(GregorianCalendar().timeInMillis) }

    fun onBtnReviewClick() {
        buttonReviewClicked.value = true
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel(CancellationException("Cancelled on onCleared"))
    }
}