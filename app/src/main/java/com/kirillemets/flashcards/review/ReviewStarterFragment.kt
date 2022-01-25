package com.kirillemets.flashcards.review

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.databinding.FragmentReviewStarterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ReviewStarterFragment : Fragment() {
    private val viewModel: ReviewFragmentViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        val binding = FragmentReviewStarterBinding.inflate(inflater)

        binding.viewModel = viewModel

        binding.buttonStartReview.setOnClickListener {
            startReview()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.reviewCards.collect {
                binding.countOfCardsTextView.text =
                    resources.getString(R.string.countOfWordsToReview, it.size)
            }
        }

        binding.lifecycleOwner = this

        if (viewModel.reviewGoing) {
            navigateToReviewFragment()
        } else {
            viewModel.loadCardsToReview()
        }
        return binding.root
    }

    private fun startReview() {
        viewModel.startReview()
        navigateToReviewFragment()
    }

    private fun navigateToReviewFragment() {
        findNavController().navigate(R.id.action_reviewStarterFragment_to_reviewFragment)
    }
}