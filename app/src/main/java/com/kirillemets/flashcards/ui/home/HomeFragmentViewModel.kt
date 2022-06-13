package com.kirillemets.flashcards.ui.home

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

data class HomeUIState(
    val reviewWordCount: Int = 0
)

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val loadCardForReviewUseCase: LoadCardForReviewUseCase,
) : ViewModel() {
    private val reviewCardsSize = MutableStateFlow(0)

    val homeUIState: Flow<HomeUIState> = reviewCardsSize.map {
        HomeUIState(it)
    }

    fun loadCardCount() {
        viewModelScope.launch {
            reviewCardsSize.value = loadCardForReviewUseCase(TimeUtil.todayMillis).size
        }
    }
}
