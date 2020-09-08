package com.kirillemets.flashcards.review

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.*
import java.util.*

class ReviewFragmentViewModel(val database: CardDatabaseDao): ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    val reviewCards: MutableLiveData<List<ReviewCard>> = MutableLiveData(listOf())


    val onButtonReviewClicked = MutableLiveData(false)
    val onButtonShowAnswerClicked = MutableLiveData(false)
    val onNextWord = MutableLiveData(false)
    val onRunOutOfWords = MutableLiveData(false)


    private val reviewCardsIterator: Iterator<ReviewCard> by lazy {
        (reviewCards.value?: listOf()).iterator()
    }

    private val _currentCard: MutableLiveData<ReviewCard> = MutableLiveData()
    val currentCard: LiveData<ReviewCard>
        get() = _currentCard

    val wordReadingVisibility = Transformations.map(_currentCard) {
        if(it.wordReading.isNotEmpty()) View.VISIBLE
        else View.GONE
    }

    val answerReadingVisibility = Transformations.map(_currentCard) {
        if(it.answerReading.isNotEmpty()) View.VISIBLE
        else View.GONE
    }

    init {
        makeCardsToReview()
    }

    private fun makeCardsToReview() {
        coroutineScope.launch {
            val cards = getRelevantCardsFromDatabaseAsync().await()
            val newList = mutableListOf<ReviewCard>()
            cards.forEach { card ->
                newList.add(
                    ReviewCard(
                        card.japanese,
                        card.reading,
                        card.english,
                        "",
                        false,
                        card.lastDelay,
                        card.cardId
                    )
                )
                newList.add(
                    ReviewCard(
                        card.english,
                        "",
                        card.japanese,
                        card.reading,
                        true,
                        card.lastDelayReversed,
                        card.cardId
                    )
                )
            }
            reviewCards.value = newList.shuffled()
            _currentCard.value = reviewCardsIterator.next()
        }
    }

    private fun getRelevantCardsFromDatabaseAsync(): Deferred<List<FlashCard>> =
        coroutineScope.async(Dispatchers.IO) { database.getRelevantCards(GregorianCalendar().timeInMillis) }

    fun onButtonReviewClick() {
        onButtonReviewClicked.value = true
    }

    fun onButtonShowAnswerClick() {
        onButtonShowAnswerClicked.value = true
    }

    fun onButtonAnswerClick(buttonType: Int) {

        if(reviewCardsIterator.hasNext())
            _currentCard.value = reviewCardsIterator.next()
        else
            onRunOutOfWords.value = true

        onNextWord.value = true
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel(CancellationException("Cancelled on onCleared"))
    }
}