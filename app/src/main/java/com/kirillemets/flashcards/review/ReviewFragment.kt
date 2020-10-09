package com.kirillemets.flashcards.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.databinding.FragmentReviewBinding
import com.kirillemets.flashcards.myDictionary.MyDictionaryFragmentViewModelFactory

class ReviewFragment : Fragment() {

    private lateinit var viewModel: ReviewFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = CardDatabase.getInstance(requireContext()).flashCardsDao()
        val binding = FragmentReviewBinding.inflate(inflater)


        viewModel = ViewModelProvider(
            this,
            MyDictionaryFragmentViewModelFactory(database)
        ).get(ReviewFragmentViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.onButtonReviewClicked.observe(this, {
            if(it) {
                viewModel.onButtonReviewClicked.value = false

                binding.mainLayout.visibility = View.GONE
                binding.reviewLayout.visibility = View.VISIBLE
            }
        })

        viewModel.onButtonShowAnswerClicked.observe(this, {
            if(it) {
                viewModel.onButtonShowAnswerClicked.value = false

                binding.buttonShowAnswer.visibility = View.GONE
                binding.answerLayout.visibility = View.VISIBLE
            }
        })

        viewModel.onNextWord.observe(this, {
            if(it) {
                viewModel.onNextWord.value = false

                binding.buttonShowAnswer.visibility = View.VISIBLE
                binding.answerLayout.visibility = View.GONE
            }
        })

        viewModel.reviewCards.observe(this, {
            binding.countOfCardsTextView.text = resources.getString(R.string.countOfWordsToReview, it.size)
        })

        viewModel.currentCard.observe(this, {
            binding.hardButtonDelay.text = resources.getQuantityString(R.plurals.daysToDelay, it.lastDelay + 1, it.lastDelay + 1)
            binding.normalButtonDelay.text = resources.getQuantityString(R.plurals.daysToDelay, it.lastDelay + 2, it.lastDelay + 2)
            binding.easyButtonDelay.text = resources.getQuantityString(R.plurals.daysToDelay, it.lastDelay + 4, it.lastDelay + 4)
        })

        binding.lifecycleOwner = this

        return binding.root
    }
}