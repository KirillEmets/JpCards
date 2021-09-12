package com.kirillemets.flashcards.addWord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.databinding.FragmentAddWordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWordFragment : Fragment() {
    val viewModel: AddWordFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddWordBinding.inflate(inflater)

        val adapter = AddWordFragmentAdapter(AddWordFragmentAdapter.AddWordFragmentAdapterCallback {
            searchResult, id ->
            viewModel.onAddButtonClicked(searchResult, id)
        })

        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = this

        viewModel.flashCards.observe(viewLifecycleOwner) { list ->
            adapter.searchResultCards = list
        }

        viewModel.insertionResult.observe(viewLifecycleOwner) {
            var stringId: Int = R.string.insertion_success
            if(!it)
               stringId = R.string.insertion_failure
            Toast.makeText(requireContext(), stringId, Toast.LENGTH_SHORT).show()
        }

        binding.textField.editText?.doAfterTextChanged {
            if(!it.isNullOrBlank())
                viewModel.startSearch(it.toString())
        }

        return binding.root
    }
}