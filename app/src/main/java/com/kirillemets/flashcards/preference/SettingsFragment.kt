package com.kirillemets.flashcards.preference

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kirillemets.flashcards.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        
        findPreference<MaterialSliderPreference>("hard_multiplier")!!.apply {
            provideFragmentManager(requireActivity().supportFragmentManager)
            setBounds(0.1f, 5f, 0.1f)
        }

        findPreference<MaterialSliderPreference>("easy_multiplier")!!.apply {
            provideFragmentManager(requireActivity().supportFragmentManager)
            setBounds(0.1f, 5f, 0.1f)
        }

        findPreference<MaterialSliderPreference>("review_font_size")!!.apply {
            provideFragmentManager(requireActivity().supportFragmentManager)
            setBounds(22f, 36f, 2f)
        }

        findPreference<ListPreference>("theme")!!.apply {
            setOnPreferenceChangeListener { _: Preference, _: Any ->
                requireActivity().finish()
                startActivity(requireActivity().intent)
                true
            }
        }
    }
}