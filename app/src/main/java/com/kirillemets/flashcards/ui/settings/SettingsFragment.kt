package com.kirillemets.flashcards.ui.settings

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
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.domain.model.ExportDestination
import com.kirillemets.flashcards.domain.model.ExportInfo
import com.kirillemets.flashcards.domain.model.ExportSchema
import com.kirillemets.flashcards.registerForSuspendableActivityResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime

@SuppressLint("InflateParams")
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    val viewModel: SettingsFragmentViewModel by viewModels()

    private val exportAlertDialog by lazy {
        val exportTypes = mapOf(
            "With progress" to true,
            "Without progress" to false
        )

        val exportTypeMap = mapOf(
            "Share" to ExportDestination.Share,
            "To download folder" to ExportDestination.SaveToFile
        )

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
            val items = exportTypeMap.keys.toList()
            setText(items[0])

            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, items)
            setAdapter(adapter)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Export")
            .setView(view)
            .setPositiveButton("Export") { _, _ ->
                val withProgress = exportTypes[editTextType.text.toString()] ?: false
                startExport(withProgress, exportTypeMap[editTextDestination.text.toString()]!!)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()
    }

    private val overwriteAlertDialog by lazy {
        AlertDialog
            .Builder(context)
            .setTitle("Override cards")
            .setMessage("This option will delete all your cards and replace them with the cards from the chosen file.")
            .setPositiveButton("Ok") { _, _ ->
                viewModel.overrideCards()
            }
            .setNegativeButton("Cancel") { _, _ ->

            }
            .create()
    }

    private val addAlertDialog by lazy {
        AlertDialog
            .Builder(context)
            .setTitle("Add cards")
            .setMessage("This option will add all the cards from the chosen file to your dictionary. Existing cards will remain.")
            .setPositiveButton("Ok") { _, _ ->
                viewModel.addCards()
            }
            .setNegativeButton("Cancel") { _, _ ->

            }
            .create()
    }

    private val requestPermissionLauncher =
        registerForSuspendableActivityResult(ActivityResultContracts.RequestPermission())

    private val openImportedDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let onDocument@{ uri ->
                context?.contentResolver?.openInputStream(uri)?.let { inputStream ->
                    try {
                        viewModel.setImportedCards(CSVImporter().import(inputStream))

                        if(viewModel.importedCards.isEmpty())
                            return@onDocument

                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Import ${viewModel.importedCards.size} cards.")
                            .setItems(arrayOf("Add", "Overwrite")) { di, i ->
                                when (i) {
                                    0 -> addAlertDialog.show()
                                    1 -> overwriteAlertDialog.show()
                                }
                            }
                            .setNegativeButton("Cancel") { _, _ -> }
                            .create()
                            .show()

                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to load", Toast.LENGTH_SHORT).show()
                        Log.e("ImportFragment", e.message ?: "")
                        return@onDocument
                    }
                }
            }
        }

    private suspend fun tryStoragePermissions(destination: ExportDestination): Boolean {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && destination == ExportDestination.SaveToFile) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return requestPermissionLauncher.launchAndAwait(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        return true
    }

    private fun startExport(withProgress: Boolean, destination: ExportDestination) {
        MainScope().launch() {

            if (!tryStoragePermissions(destination))
                return@launch

            val tag = if (withProgress) "with_progress" else "without_progress"
            val filename = "${tag}-${LocalDateTime.now().toString("yyyy-MM-dd-HH_mm")}"

            viewModel.exportNotes(ExportInfo(destination, withProgress, filename, ExportSchema.CSV))
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        viewModel.setImportedCards(listOf())

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

        findPreference<Preference>("import_from_file")!!.apply {
            setOnPreferenceClickListener {
                openImportedDocumentLauncher.launch(arrayOf("*/*"))
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