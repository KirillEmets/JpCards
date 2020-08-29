package com.kirillemets.flashcards.addWord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.databinding.FragmentAddWordBinding

class AddWordFragment : Fragment() {


    private val viewModel: AddWordFragmentViewModel by lazy {
        ViewModelProvider(this).get(AddWordFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddWordBinding.inflate(inflater)

        val adapter = AddWordFragmentAdapter()
        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = this

        viewModel.flashCards.observe(this, { list ->
            adapter.searchResultCards = list
        })

        binding.textField.editText?.doOnTextChanged {
            text, _, _, _ -> viewModel.onSearchInputTextChanged(text?.toString() ?: "")
        }

        viewModel.search("make")
        return binding.root
    }
}