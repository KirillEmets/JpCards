package com.kirillemets.flashcards.review

import androidx.lifecycle.*
import com.kirillemets.flashcards.TimeUtil
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import kotlin.math.roundToInt

class ReviewFragmentViewModel(val database: CardDatabaseDao): ViewModel() {
    var delayEasyMultiplier = 1f
    var delayHardMultiplier = 1f

    val reviewCards: MutableLiveData<List<ReviewCard>> = MutableLiveData(listOf())

    private val _currentCard: MutableLiveData<ReviewCard> = MutableLiveData()
    val currentCard: LiveData<ReviewCard>
        get() = _currentCard

    val reviewStarted = MutableLiveData(false)
    val answerShown = MutableLiveData(false)

    val fontSizeBig = MutableLiveData(30)
    val fontSizeSmall = MutableLiveData(30)

    val buttonReviewClickable = Transformations.map(reviewCards){
        it.isNotEmpty()
    }

    var wordCounter = MutableLiveData(0)
    val counterText = Transformations.map(wordCounter) {
        "${it + 1} / ${reviewCards.value?.size ?: 0}"
    }

    fun loadCardsToReview() {
        viewModelScope.launch {
            val currentTime = LocalDate.now().toDateTimeAtStartOfDay().millis
            val cards = getRelevantCardsFromDatabase(currentTime)
            val newList = mutableListOf<ReviewCard>()
            cards.forEach { card ->
                if(card.nextReviewTime <= currentTime)
                    newList.add(ReviewCard.fromDataBaseFlashCardDefault(card))
                if(card.nextReviewTimeReversed <= currentTime)
                    newList.add(ReviewCard.fromDataBaseFlashCardReversed(card))
            }
            reviewCards.value = newList.shuffled()
            if(newList.isNotEmpty()) {
                wordCounter.value = 0
                _currentCard.value = reviewCards.value!![0]
            }
        }
    }

    private fun onRunOutOfWords() {
        reviewStarted.value = false
        loadCardsToReview()
    }

    private suspend fun getRelevantCardsFromDatabase(time: Long): List<FlashCard> =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            database.getRelevantCards(time)
        }

    fun onButtonReviewClick() {
        reviewStarted.value = true
    }

    fun onButtonShowAnswerClick() {
        answerShown.value = true
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
            val newDelay: Int = getNewDelay(card.lastDelay, buttonType)

            val nextRepeatTime: Long =
                LocalDate.now().toDateTimeAtStartOfDay().plusDays(newDelay).millis

            viewModelScope.launch(Dispatchers.IO) {
                if (!card.reversed)
                    database.updateRegularDelayAndTime(card.cardId, newDelay, nextRepeatTime)
                else
                    database.updateReversedDelayAndTime(card.cardId, newDelay, nextRepeatTime)
            }
        }

        wordCounter.value = wordCounter.value?.plus(1)
        if (wordCounter.value!! < reviewCards.value!!.size)
            _currentCard.value = reviewCards.value!![wordCounter.value!!]

        else
            onRunOutOfWords()

        answerShown.value = false
    }

    // 1 - easy, 2 - hard
    fun getNewDelay(lastDelay: Int, difficulty: Int): Int =
        (lastDelay * when(difficulty) {
            1 -> delayEasyMultiplier
            2 -> delayHardMultiplier
            else -> 1f
        }).roundToInt()

}