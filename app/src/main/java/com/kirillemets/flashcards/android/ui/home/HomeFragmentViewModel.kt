package com.kirillemets.flashcards.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillemets.flashcards.shared.domain.usecase.LoadCardForReviewUseCase
import com.kirillemets.flashcards.android.ui.TimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
