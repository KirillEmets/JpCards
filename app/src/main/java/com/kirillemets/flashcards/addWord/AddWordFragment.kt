package com.kirillemets.flashcards.addWord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.databinding.FragmentAddWordBinding

class AddWordFragment : Fragment() {


    private val viewModel: AddWordFragmentViewModel by lazy {
        ViewModelProvider(this).get(AddWordFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddWordBinding.inflate(inflater)
        viewModel.database = CardDatabase.getInstance(requireContext()).flashCardsDao()
        val adapter = AddWordFragmentAdapter(AddWordFragmentAdapter.AddWordFragmentAdapterCallback {
            searchResult, id ->
            viewModel.onAddButtonClicked(searchResult, id)
        })

        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = this

        viewModel.flashCards.observe(viewLifecycleOwner, { list ->
            adapter.searchResultCards = list
        })

        binding.textField.editText?.doAfterTextChanged {
            if(!it.isNullOrBlank())
                viewModel.startSearch(it.toString())
        }

        return binding.root
    }
}