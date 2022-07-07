package com.kirillemets.flashcards.ui.myDictionary

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.ui.TimeUtil
import com.kirillemets.flashcards.databinding.FragmentMyDictionaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDictionaryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    val viewModel: MyDictionaryFragmentViewModel by viewModels()
    lateinit var binding: FragmentMyDictionaryBinding
    private val adapter: MyDictionaryFragmentAdapter = MyDictionaryFragmentAdapter {
        viewModel.onItemClick(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyDictionaryBinding.inflate(layoutInflater)
        adapter.currentTimeMillis = TimeUtil.todayMillis

        binding.viewModel = viewModel
        binding.recyclerView.adapter = adapter


        lifecycleScope.launchWhenStarted {
            viewModel.myWordsUIState.collect {
                adapter.notes = it.notes
                if(it.ttsFailed) {
                    Toast.makeText(context, "Tts service failed", Toast.LENGTH_SHORT).show()
                    viewModel.resetTtsFailed()
                }
            }
        }

        binding.textField.editText?.doAfterTextChanged {
            it?.let {
                viewModel.filterWords(it.toString())
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_dictionary_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onMenuItemSelected(item)
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.item_remove -> {
                viewModel.deleteWords(adapter.checkedCards.toSet())
                adapter.uncheckAllCards()
                true
            }
            R.id.item_reset -> {
                viewModel.resetWords(adapter.checkedCards.toSet())
                adapter.uncheckAllCards()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}