package com.kirillemets.flashcards.preference

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.model.FlashCardRepository
import com.kirillemets.flashcards.importExport.CSVExporter
import com.kirillemets.flashcards.importExport.exportToStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private var lastExportChoice: String? = null

    @Inject
    lateinit var flashCardRepository: FlashCardRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        fun exportToFile(choice: String?) {
            requireActivity().lifecycleScope.launch(Dispatchers.IO) {
                val cards = flashCardRepository.getAllSuspend()
                val withProgress = choice == "with_progress"
                val exporter = CSVExporter(withProgress)
                val name =
                    "flashcards-${choice}-${LocalDateTime.now().toString("yyyy-MM-dd-HH_mm")}"
                exportToStorage(cards, name, exporter, requireContext())
            }
        }

        val request = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            exportToFile(lastExportChoice)
        }

        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<MaterialSliderPreference>("miss_multiplier")!!.apply {
            provideFragmentManager(requireActivity().supportFragmentManager)
            setBounds(0.0f, 0.4f, 0.1f)
        }

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
                } else {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        exportToFile(newValue as String)
                    } else {
                        request.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
                true
            }
        }

        findPreference<Preference>("import_from_file")!!.apply {
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_importFragment)
                true
            }
        }

        findPreference<Preference>("about_developer")!!.apply {
            setOnPreferenceClickListener {

                val message =
                    SpannableString("Author: Kirill Yemets" + "\nGitHub: https://github.com/KirillEmets/japaneseflashcards")
                Linkify.addLinks(message, Linkify.ALL)

                val ad = AlertDialog.Builder(context)
                    .setTitle("About developer")
                    .setMessage(message)
                    .create()

                ad.show()
                ad.findViewById<TextView>(android.R.id.message).movementMethod = LinkMovementMethod.getInstance()
                true
            }
        }

        findPreference<Preference>("github")!!.apply {
            setOnPreferenceClickListener {
                val url = "https://github.com/KirillEmets/japaneseflashcards"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                true
            }
        }
    }
}