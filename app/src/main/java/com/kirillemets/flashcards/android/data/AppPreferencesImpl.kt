package com.kirillemets.flashcards.android.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.kirillemets.flashcards.shared.domain.AppPreferences

class AppPreferencesImpl(appContext: Context) : AppPreferences {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(appContext)

    override var delayMissMultiplier: Int
        get() = prefs.getString(KEY_MISS_MULTIPLIER, "10")?.toInt() ?: 10
        set(value) = prefs.edit().putString(KEY_MISS_MULTIPLIER, value.toString()).apply()


    override var delayEasyMultiplier: Int
        get() = prefs.getString(KEY_EASY_MULTIPLIER, "80")?.toInt() ?: 80
        set(value) = prefs.edit().putString(KEY_EASY_MULTIPLIER, value.toString()).apply()

    override var delayHardMultiplier: Int
        get() = prefs.getString(KEY_HARD_MULTIPLIER, "200")?.toInt() ?: 200
        set(value) = prefs.edit().putString(KEY_HARD_MULTIPLIER, value.toString()).apply()

    override var theme: AppPreferences.Theme
        get() = when (prefs.getString(KEY_THEME, "auto")) {
            "Light" -> AppPreferences.Theme.Light
            "Black" -> AppPreferences.Theme.Black
            else -> AppPreferences.Theme.Auto
        }
        set(value) = when (value) {
            AppPreferences.Theme.Auto -> "Auto"
            AppPreferences.Theme.Light -> "Light"
            AppPreferences.Theme.Black -> "Black"
        }.let {
            prefs.edit().putString(KEY_THEME, it)
                .apply()
        }


    companion object {
        const val KEY_MISS_MULTIPLIER = "miss_multiplier"
        const val KEY_EASY_MULTIPLIER = "easy_multiplier"
        const val KEY_HARD_MULTIPLIER = "hard_multiplier"

        const val KEY_THEME = "theme"
    }
}