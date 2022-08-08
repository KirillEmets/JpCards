package com.kirillemets.flashcards.android.ui.addWord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kirillemets.flashcards.android.R
import com.kirillemets.flashcards.android.databinding.FragmentAddWordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddWordFragment : Fragment() {
    val viewModel: AddWordFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddWordBinding.inflate(inflater)

        val adapter =
            AddWordFragmentAdapter(AddWordFragmentAdapter.AddWordFragmentAdapterCallback { searchResult, id ->
                viewModel.onAddButtonClicked(searchResult, id)
            })

        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.entries.collect { list ->
                    adapter.dictionaryEntries = list

                    binding.hintTextView.visibility =
                        if (list.isEmpty() && binding.textField.editText?.text.isNullOrBlank()) View.VISIBLE else View.INVISIBLE

                }
            }

            launch {
                viewModel.insertionResult.collect {
                    it?.let {
                        val stringId =
                            if (it)
                                R.string.insertion_success
                            else R.string.insertion_failure

                        Toast.makeText(requireContext(), stringId, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        binding.textField.editText?.doAfterTextChanged {
            if (!it.isNullOrBlank())
                viewModel.startSearch(it.toString())
            else
                viewModel.clearList()
        }

        return binding.root
    }
}