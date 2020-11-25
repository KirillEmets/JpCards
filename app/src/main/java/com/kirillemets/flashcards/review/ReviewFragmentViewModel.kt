package com.kirillemets.flashcards.review

import android.view.View
import androidx.lifecycle.*
import com.kirillemets.flashcards.TimeUtil
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.*
import org.joda.time.LocalDate

class ReviewFragmentViewModel(val database: CardDatabaseDao): ViewModel() {

    companion object {
        const val DELAY_EASY = 1
        const val DELAY_NORMAL = 3
        const val DELAY_HARD = 5
    }

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

    private var wordReadingVisible: Boolean = false
    val wordReadingVisibility = Transformations.map(_currentCard) {
        if(it.wordReading.isNotEmpty() && wordReadingVisible) View.VISIBLE
        else View.GONE
    }

    val answerReadingVisibility = Transformations.map(_currentCard) {
        if(it.answerReading.isNotEmpty()) View.VISIBLE
        else View.GONE
    }

    val buttonReviewClickable = Transformations.map(reviewCards){
        it.isNotEmpty()
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
            if(newList.isNotEmpty())
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
        wordReadingVisible = true
        _currentCard.value = _currentCard.value
    }

    fun onButtonAnswerClick(buttonType: Int) {
        val card: ReviewCard = _currentCard.value!!
        if (buttonType == 0) {
            viewModelScope.launch(Dispatchers.IO) {
                if(!card.reversed)
                    database.resetDelayByIds(setOf(card.cardId), TimeUtil.todayMillis)
                else
                    database.resetDelayByIdsReversed(setOf(card.cardId), TimeUtil.todayMillis)

            }
        }
        else {
            val newDelay = when (buttonType) {
                1 -> card.lastDelay + DELAY_EASY
                2 -> card.lastDelay + DELAY_NORMAL
                3 -> card.lastDelay + DELAY_NORMAL
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

        wordReadingVisible = false

        if (reviewCardsIterator.hasNext())
            _currentCard.value = reviewCardsIterator.next()
        else
            onRunOutOfWords.value = true

        onNextWord.value = true
    }
}