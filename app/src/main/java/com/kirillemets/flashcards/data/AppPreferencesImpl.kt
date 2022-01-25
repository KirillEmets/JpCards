package com.kirillemets.flashcards.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.kirillemets.flashcards.domain.AppPreferences

class AppPreferencesImpl(appContext: Context) : AppPreferences {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(appContext)

    override var delayMissMultiplier: Float
        get() = prefs.getFloat(KEY_MISS_MULTIPLIER, 0f)
        set(value) = prefs.edit().putFloat(KEY_MISS_MULTIPLIER, value).apply()


    override var delayEasyMultiplier: Float
        get() = prefs.getFloat(KEY_EASY_MULTIPLIER, 0f)
        set(value) = prefs.edit().putFloat(KEY_EASY_MULTIPLIER, value).apply()

    override var delayHardMultiplier: Float
        get() = prefs.getFloat(KEY_HARD_MULTIPLIER, 0f)
        set(value) = prefs.edit().putFloat(KEY_HARD_MULTIPLIER, value).apply()


    companion object {
        const val KEY_MISS_MULTIPLIER = "miss_multiplier"
        const val KEY_EASY_MULTIPLIER = "easy_multiplier"
        const val KEY_HARD_MULTIPLIER = "hard_multiplier"
    }
}
