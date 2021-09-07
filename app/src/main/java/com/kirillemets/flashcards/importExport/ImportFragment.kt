package com.kirillemets.flashcards.importExport

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.database.FlashCardRepository
import com.kirillemets.flashcards.databinding.FragmentImportBinding

class ImportFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImportBinding.inflate(inflater)
        val importer = CSVImporter()
        val repository = FlashCardRepository(requireContext())
        val viewModel = ViewModelProvider(
            this,
            ImportFragmentViewModelFactory(repository)
        ).get(ImportFragmentViewModel::class.java)

        val openDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let onDocument@{ uri ->
                context?.contentResolver?.openInputStream(uri)?.let { inputStream ->
                    try {
                        viewModel.setImportedCards(importer.import(inputStream))
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to load", Toast.LENGTH_SHORT).show()
                        Log.e("ImportFragment", e.message ?: "")
                        return@onDocument
                    }
                }
            }
        }

        binding.btnChooseFile.setOnClickListener {
            openDocument.launch(arrayOf("*/*"))
        }

        binding.btnOverride.setOnClickListener {
            AlertDialog
                .Builder(context)
                .setTitle("Override cards")
                .setMessage("This option will delete all your cards and replace them with the cards from the chosen file.")
                .setPositiveButton("Ok") { _, _ ->
                    viewModel.overrideCards()
                }
                .setNegativeButton("Cancel") {_, _ ->

                }
                .create().show()
        }

        binding.btnAdd.setOnClickListener {
            AlertDialog
                .Builder(context)
                .setTitle("Add cards")
                .setMessage("This option will add all the cards from the chosen file to your dictionary. Existing cards will remain.")
                .setPositiveButton("Ok") { _, _ ->
                    viewModel.addCards()
                }
                .setNegativeButton("Cancel") {_, _ ->

                }
                .create().show()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}