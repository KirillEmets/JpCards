package com.kirillemets.flashcards.review

import android.view.View
import androidx.lifecycle.*
import com.kirillemets.flashcards.TimeUtil
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.*
import org.joda.time.LocalDate

class ReviewFragmentViewModel(val database: CardDatabaseDao): ViewModel() {

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
        viewModelScope.launch {
            val currentTime = LocalDate.now().toDateTimeAtStartOfDay().millis
            val cards = getRelevantCardsFromDatabaseAsync(currentTime).await()
            val newList = mutableListOf<ReviewCard>()
            cards.forEach { card ->
                if(card.nextReviewTime <= currentTime)
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
                if(card.nextReviewTimeReversed <= currentTime)
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

    private fun getRelevantCardsFromDatabaseAsync(time: Long): Deferred<List<FlashCard>> =
        viewModelScope.async(Dispatchers.IO) { database.getRelevantCards(time) }

    fun onButtonReviewClick() {
        onButtonReviewClicked.value = true
    }

    fun onButtonShowAnswerClick() {
        onButtonShowAnswerClicked.value = true
    }

    fun onButtonAnswerClick(buttonType: Int) {
        val card: ReviewCard = _currentCard.value!!
        if (buttonType == 0) {
            viewModelScope.launch(Dispatchers.IO) {
                database.resetDelayByIds(setOf(card.cardId), TimeUtil.todayMillis)
            }
        }
        else {
            val newDelay = when (buttonType) {
                1 -> card.lastDelay + 1
                2 -> card.lastDelay + 2
                3 -> card.lastDelay + 4
                else -> 0
            }

            val nextRepeatTime: Long =
                LocalDate.now().toDateTimeAtStartOfDay().plusDays(newDelay).millis

            viewModelScope.launch(Dispatchers.IO) {
                if (!card.reversed)
                    database.updateRegularDelayAndTime(card.cardId, newDelay, nextRepeatTime)
                else
                    database.updateReversedDelayAndTime(card.cardId, newDelay, nextRepeatTime)
            }
        }

        if (reviewCardsIterator.hasNext())
            _currentCard.value = reviewCardsIterator.next()
        else
            onRunOutOfWords.value = true

        onNextWord.value = true
    }
}