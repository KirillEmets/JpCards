package com.kirillemets.flashcards.android.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kirillemets.flashcards.android.R
import com.kirillemets.flashcards.android.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        val binding = FragmentHomeBinding.inflate(inflater)

        viewModel.loadCardCount()

        lifecycleScope.launchWhenStarted {
            viewModel.homeUIState.collect { homeUIState ->
                binding.countOfCardsTextView.text =
                    resources.getString(R.string.countOfWordsToReview, homeUIState.reviewWordCount)

                if(homeUIState.reviewWordCount == 0) {
                    binding.buttonStartReview.text = "Add new words"
                    binding.buttonStartReview.setOnClickListener {
                        navigateToAddWordsFragment()
                    }
                }
                else {
                    binding.buttonStartReview.text = "Review"
                    binding.buttonStartReview.setOnClickListener {
                        navigateToReviewFragment()
                    }
                }

                binding.uiState = homeUIState
            }
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    private fun navigateToReviewFragment() {
        findNavController().navigate(R.id.action_reviewStarterFragment_to_reviewFragment)
    }

    private fun navigateToAddWordsFragment() {
        findNavController().navigate(R.id.action_global_addWordFragment)
    }
}