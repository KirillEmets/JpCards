package com.kirillemets.flashcards.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillemets.flashcards.domain.model.AnswerType
import com.kirillemets.flashcards.domain.model.ReviewCard
import com.kirillemets.flashcards.domain.usecase.DeleteCardsWithIndexesUseCase
import com.kirillemets.flashcards.domain.usecase.GetNewDelayInDaysUseCase
import com.kirillemets.flashcards.domain.usecase.LoadCardForReviewUseCase
import com.kirillemets.flashcards.domain.usecase.UpdateCardWithAnswerUseCase
import com.kirillemets.flashcards.ui.TimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDate
import java.util.*
import javax.inject.Inject

data class ReviewUIState(
    val currentCard: ReviewCardUIState = ReviewCardUIState(),
    val showAnswer: Boolean = false,
    val currentWordNumber: Int = 0,
    val wordCount: Int = 0,
    val missDelay: Int = 0,
    val easyDelay: Int = 0,
    val hardDelay: Int = 0,
)

@HiltViewModel
class ReviewFragmentViewModel @Inject constructor(
    private val loadCardForReviewUseCase: LoadCardForReviewUseCase,
    private val updateCardWithAnswerUseCase: UpdateCardWithAnswerUseCase,
    private val getNewDelayUseCase: GetNewDelayInDaysUseCase,
    private val deleteCardsWithIndexesUseCase: DeleteCardsWithIndexesUseCase
) : ViewModel() {

    private val today = TimeUtil.todayMillis
    private val reviewCards = MutableStateFlow(emptyList<ReviewCard>())

    init {
        viewModelScope.launch {
            reviewCards.value =
                loadCardForReviewUseCase(today).shuffled().sortedByDescending { it.lastDelay }
        }
    }

    private var wordCounter = MutableStateFlow(0)
    private val currentCard = combine(
        wordCounter,
        reviewCards
    ) { counter, cards ->
        if (cards.isNotEmpty())
            cards[counter]
        else ReviewCard.Empty
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ReviewCard.Empty
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
            ReviewUIState()
        )

    val onRunOutOfWords = MutableStateFlow(false)


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
        val deleteId = currentCard.value.noteId
        val new = reviewCards.value.toMutableList()
        var wc = wordCounter.value

        if (new.indexOfFirst { card -> card.noteId == deleteId } < wc)
            wc -= 1

        new.removeAll { card -> card.noteId == deleteId }

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

    private fun onRunOutOfWords() {
        onRunOutOfWords.value = true
    }
}
