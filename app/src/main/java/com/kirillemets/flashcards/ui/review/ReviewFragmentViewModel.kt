package com.kirillemets.flashcards.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillemets.flashcards.domain.model.AnswerType
import com.kirillemets.flashcards.domain.model.ReviewCard
import com.kirillemets.flashcards.domain.uselesscase.DeleteCardsWithIndexesUseCase
import com.kirillemets.flashcards.domain.uselesscase.GetNewDelayInDaysUseCase
import com.kirillemets.flashcards.domain.uselesscase.LoadCardForReviewUseCase
import com.kirillemets.flashcards.domain.uselesscase.UpdateCardWithAnswerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.joda.time.LocalDate
import javax.inject.Inject

data class ReviewUIState(
    val currentCard: ReviewCardUIState,
    val showAnswer: Boolean,
    val currentWordNumber: Int,
    val wordCount: Int,
    val missDelay: Int,
    val easyDelay: Int,
    val hardDelay: Int,
)

@HiltViewModel
class ReviewFragmentViewModel @Inject constructor(
    private val loadCardForReviewUseCase: LoadCardForReviewUseCase,
    private val updateCardWithAnswerUseCase: UpdateCardWithAnswerUseCase,
    private val getNewDelayUseCase: GetNewDelayInDaysUseCase,
    private val deleteCardsWithIndexesUseCase: DeleteCardsWithIndexesUseCase
) : ViewModel() {

    val reviewCards = MutableStateFlow(emptyList<ReviewCard>())
    private var wordCounter = MutableStateFlow<Int>(0)
    private val currentCard = combine(
        wordCounter,
        reviewCards
    ) { counter, cards ->
        if (cards.isNotEmpty())
            cards[counter]
        else ReviewCard("", "", "", "", false, 0, 0)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ReviewCard("", "", "", "", false, 0, 0)
    )

    private val showAnswer = MutableStateFlow(false)

    val reviewUIState: Flow<ReviewUIState> =
        combine(currentCard, showAnswer, wordCounter, reviewCards) { card, show, counter, all ->
            ReviewUIState(
                ReviewCardUIState.fromReviewCard(card),
                show,
                counter + 1,
                all.size,
                getNewDelayUseCase(card.lastDelay, AnswerType.Miss),
                getNewDelayUseCase(card.lastDelay, AnswerType.Easy),
                getNewDelayUseCase(card.lastDelay, AnswerType.Hard),
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ReviewUIState(ReviewCardUIState(), false, 0, 0, 0, 0, 0)
        )

    var reviewGoing = false

    val buttonReviewClickable = reviewCards.transform { cards -> emit(cards.isNotEmpty()) }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    val onRunOutOfWords = MutableStateFlow(false)

    private val today = LocalDate.now().toDateTimeAtStartOfDay().millis

    fun loadCardsToReview() {
        viewModelScope.launch {
            reviewCards.value = loadCardForReviewUseCase(DateTime().millis)
            wordCounter.value = 0
        }
    }

    fun onButtonShowAnswerClick() {
        showAnswer.value = true
    }

    fun onButtonAnswerClick(buttonType: Int) {
        val card: ReviewCard = currentCard.value

        val at = when (buttonType) {
            0 -> AnswerType.Miss
            1 -> AnswerType.Easy
            2 -> AnswerType.Hard
            else -> AnswerType.Miss
        }

        viewModelScope.launch {
            updateCardWithAnswerUseCase(card, at, today)
        }

        showNextCard()
    }

    private fun showNextCard() {
        if (wordCounter.value + 1 == reviewCards.value.size)
            return onRunOutOfWords()

        wordCounter.value += 1
        showAnswer.value = false
    }

    fun deleteCurrentCard() {
        val deleteId = currentCard.value.cardId
        val new = reviewCards.value.toMutableList()
        var wc = wordCounter.value

        if (new.indexOfFirst { card -> card.cardId == deleteId } < wc)
            wc -= 1

        new.removeAll { card -> card.cardId == deleteId }

        if (new.size <= wc) {
            runBlocking {
                deleteCardsWithIndexesUseCase(deleteId)
            }
            return onRunOutOfWords()
        }

        reviewCards.value = new


        wordCounter.value = wc
        showAnswer.value = false

        viewModelScope.launch() {
            deleteCardsWithIndexesUseCase(deleteId)
        }
    }

    fun startReview() {
        reviewGoing = true
    }

    fun endReview() {
        showAnswer.value = false
        reviewGoing = false
    }

    private fun onRunOutOfWords() {
        endReview()
        onRunOutOfWords.value = true
    }
}