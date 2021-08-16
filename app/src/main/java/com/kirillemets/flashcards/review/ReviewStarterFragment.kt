package com.kirillemets.flashcards.review

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.database.DatabaseRepository
import com.kirillemets.flashcards.databinding.FragmentReviewStarterBinding

class ReviewStarterFragment : Fragment() {
    private lateinit var viewModel: ReviewFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        val binding = FragmentReviewStarterBinding.inflate(inflater)
        val database = CardDatabase.getInstance(requireContext()).flashCardsDao()

        viewModel = ViewModelProvider(
            requireActivity(),
            ReviewFragmentViewModelFactory(DatabaseRepository(database))
        ).get(ReviewFragmentViewModel::class.java)

        binding.viewModel = viewModel

        binding.buttonStartReview.setOnClickListener {
            startReview()
        }

        viewModel.reviewCards.observe(viewLifecycleOwner, {
            binding.countOfCardsTextView.text =
                resources.getString(R.string.countOfWordsToReview, it.size)
        })

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