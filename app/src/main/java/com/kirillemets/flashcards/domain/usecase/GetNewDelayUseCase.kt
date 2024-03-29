package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.AppPreferences
import com.kirillemets.flashcards.domain.model.AnswerType
import javax.inject.Inject
import kotlin.math.roundToInt

class GetNewDelayInDaysUseCase @Inject constructor(private val appPreferences: AppPreferences) {
    operator fun invoke(
        lastDelay: Int,
        answerType: AnswerType,
    ): Int {
        if (lastDelay == 0) return if (answerType == AnswerType.Miss) 0 else 1

        val newDelay = (lastDelay * when (answerType) {
            AnswerType.Miss -> appPreferences.delayMissMultiplier
            AnswerType.Easy -> appPreferences.delayEasyMultiplier
            AnswerType.Hard -> appPreferences.delayHardMultiplier
        } / 100f).roundToInt()

        return newDelay
    }
}