package com.kirillemets.flashcards.android.ui.myDictionary

import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.kirillemets.flashcards.android.R
import com.kirillemets.flashcards.android.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDictionaryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    val viewModel: MyDictionaryFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppTheme {
                MyDictionaryScreen(onButtonAddWordsClick = {
                    findNavController().navigate(R.id.action_global_addWordFragment)
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_dictionary_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onMenuItemSelected(item)
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_remove -> {
                viewModel.onDeleteSelectedWords()
                true
            }
            R.id.item_reset -> {
                viewModel.onResetSelectedWords()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}