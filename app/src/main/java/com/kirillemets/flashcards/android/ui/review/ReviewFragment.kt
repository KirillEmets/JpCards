package com.kirillemets.flashcards.android.ui.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kirillemets.flashcards.android.R
import com.kirillemets.flashcards.android.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private val viewModel: ReviewFragmentViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val binding = FragmentReviewBinding.inflate(inflater)

        binding.viewModel = viewModel

        lifecycleScope.launchWhenStarted {

            launch {
                viewModel.reviewUIState.collect { uiState ->
                    binding.uiState = uiState

                    with(uiState) {
                        binding.missButtonDelay.text =
                            if (missDelay == 0) "discard" else resources.getQuantityString(
                                R.plurals.daysToDelay, missDelay, missDelay
                            )
                        binding.easyButtonDelay.text = resources.getQuantityString(
                            R.plurals.daysToDelay, easyDelay, easyDelay
                        )
                        binding.hardButtonDelay.text = resources.getQuantityString(
                            R.plurals.daysToDelay, hardDelay, hardDelay
                        )
                        binding.textCounter.text = "$currentWordNumber / $wordCount"
                    }
                }
            }

            launch {
                viewModel.onRunOutOfWords.collect {
                    if (it) {
                        findNavController().navigateUp()
                        viewModel.onRunOutOfWords.value = false
                    }
                }
            }
        }

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.review_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_card -> {
                viewModel.deleteCurrentCard()
                return true
            }
        }

        return false
    }
}

