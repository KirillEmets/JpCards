package com.kirillemets.flashcards.addWord

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.databinding.FragmentAddWordBinding

class AddWordFragment : Fragment() {
    private val viewModel: AddWordFragmentViewModel by lazy {
        ViewModelProvider(this).get(AddWordFragmentViewModel::class.java)
    }

    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddWordBinding.inflate(inflater)
        viewModel.database = CardDatabase.getInstance(requireContext()).flashCardsDao()
        val adapter =
            AddWordFragmentAdapter(AddWordFragmentAdapter.AddWordFragmentAdapterCallback { searchResult, id ->
                viewModel.onAddButtonClicked(searchResult, id)
            })

        binding.recyclerView.adapter = adapter
        binding.lifecycleOwner = this

        viewModel.flashCards.observe(viewLifecycleOwner, { list ->
            adapter.searchResultCards = list
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_word_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        (menu.findItem(R.id.item_search_add_word)?.actionView as SearchView).apply {
            setQuery("", false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isBlank())
                        return true

                    viewModel.startSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isBlank())
                        return true

                    viewModel.startSearch(newText, false)
                    return true
                }
            })
        }
    }
}