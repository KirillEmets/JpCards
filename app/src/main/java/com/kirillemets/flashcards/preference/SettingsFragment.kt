package com.kirillemets.flashcards.preference

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.importExport.CSVExporter
import com.kirillemets.flashcards.importExport.exportToStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime

class SettingsFragment : PreferenceFragmentCompat() {

    private var lastExportChoice: String? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        fun exportToFile(choice: String?) {
            val db = CardDatabase.getInstance(requireContext()).flashCardsDao()
            requireActivity().lifecycleScope.launch(Dispatchers.IO) {
                val cards = db.getAllBlocking()
                val withProgress = choice == "with_progress"
                val exporter = CSVExporter(withProgress)
                val name = "flashcards-${choice}-${LocalDateTime.now().toString("yyyy-MM-dd-HH_mm")}"
                exportToStorage(cards, name, exporter, requireContext())
            }
        }

        val request = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            exportToFile(lastExportChoice)
        }

        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        
        findPreference<MaterialSliderPreference>("hard_multiplier")!!.apply {
            provideFragmentManager(requireActivity().supportFragmentManager)
            setBounds(0.1f, 5f, 0.1f)
        }

        findPreference<MaterialSliderPreference>("easy_multiplier")!!.apply {
            provideFragmentManager(requireActivity().supportFragmentManager)
            setBounds(0.1f, 5f, 0.1f)
        }

        findPreference<ListPreference>("theme")!!.apply {
            setOnPreferenceChangeListener { _: Preference, _: Any ->
                requireActivity().finish()
                startActivity(requireActivity().intent)
                true
            }
        }

        findPreference<ListPreference>("export_to_file")!!.apply {
            setOnPreferenceChangeListener { _, newValue ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    exportToFile(newValue as String)
                }

                else {
                    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        exportToFile(newValue as String)
                    }
                    else {
                        request.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
                true
            }
        }
    }
}