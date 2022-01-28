package com.kirillemets.flashcards.preference

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.importExport.*
import com.kirillemets.flashcards.model.FlashCardRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDateTime
import javax.inject.Inject

@SuppressLint("InflateParams")
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    @Inject
    lateinit var flashCardRepository: FlashCardRepository

    private val exportTypes = mapOf(
        "With progress" to true,
        "Without progress" to false
    )
    private val exportDestinations = mapOf<String, suspend (ExportInfo) -> Unit>(
        "Share" to ::startExportShare,
        "To download folder" to ::startExportToFile
    )

    private val exportAlertDialog by lazy {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.export_dialog, null)

        val editTextType =
            view.findViewById<TextInputLayout>(R.id.textInput_export_type).editText as AutoCompleteTextView
        (editTextType).apply {
            val items = exportTypes.keys.toList()
            setText(items[0])

            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, items)
            setAdapter(adapter)
        }

        val editTextDestination =
            view.findViewById<TextInputLayout>(R.id.textInput_export_destination).editText as AutoCompleteTextView
        (editTextDestination).apply {
            val items = exportDestinations.keys.toList()
            setText(items[0])

            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, items)
            setAdapter(adapter)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Export")
            .setView(view)
            .setPositiveButton("Export") { _, _ ->
                val withProgress = exportTypes[editTextType.text.toString()] ?: false
                startExport(withProgress, exportDestinations[editTextDestination.text.toString()]!!)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()
    }

    private fun startExport(withProgress: Boolean, destination: suspend (ExportInfo) -> Unit) {
        requireActivity().lifecycleScope.launch(Dispatchers.IO) {
            val cards = flashCardRepository.getAllSuspend()
            val exporter = CSVExporter(withProgress)
            val tag = if (withProgress) "with_progress" else "without_progress"
            val name =
                "${tag}-${LocalDateTime.now().toString("yyyy-MM-dd-HH_mm")}"

            destination(ExportInfo(cards, exporter, name, requireContext()))
        }
    }

    private suspend fun startExportShare(exportInfo: ExportInfo) {
        try {
            val uri: Uri = saveToLocal(
                exportInfo.cards,
                exportInfo.name,
                exportInfo.exporter,
                exportInfo.context
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                flags =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            }
            val chooser = Intent.createChooser(intent, "Share")
            startActivity(chooser)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(exportInfo.context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun startExportToFile(exportInfo: ExportInfo) {
        try {
            tryExportToFile(exportInfo)
            withContext(Dispatchers.Main) {
                Toast.makeText(exportInfo.context, "File saved", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(exportInfo.context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun tryExportToFile(exportInfo: ExportInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            exportToStorage(exportInfo)
            return
        }
        if (ContextCompat.checkSelfPermission(
                exportInfo.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            exportToStorage(exportInfo)
            return
        }
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            exportToStorage(exportInfo)
        }
            .launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
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

        findPreference<Preference>("export_to_file")!!.apply {
            setOnPreferenceClickListener {
                exportAlertDialog.show()
                true
            }
        }

//        findPreference<ListPreference>("export_to_file")!!.apply {
//            setOnPreferenceChangeListener { _, newValue ->
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    exportToFile(newValue as String)
//                } else {
//                    if (ContextCompat.checkSelfPermission(
//                            requireContext(),
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        ) == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        exportToFile(newValue as String)
//                    } else {
//                        request.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    }
//                }
//                true
//            }
//        }

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
                ad.findViewById<TextView>(android.R.id.message).movementMethod =
                    LinkMovementMethod.getInstance()
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