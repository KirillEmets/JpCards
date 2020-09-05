package com.kirillemets.flashcards.review

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.databinding.FragmentReviewBinding
import com.kirillemets.flashcards.myDictionary.MyDictionaryFragmentViewModel
import com.kirillemets.flashcards.myDictionary.MyDictionaryFragmentViewModelFactory
import kotlinx.android.synthetic.main.fragment_review.*


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

        viewModel.buttonReviewClicked.observe(this, {
            if(it) {
                viewModel.buttonReviewClicked.value = false

                binding.reviewLayout.visibility = View.VISIBLE
                binding.mainLayout.visibility = View.GONE
            }
        })

        viewModel.reviewCards.observe(this, {
            binding.countOfCardsTextView.text = resources.getString(R.string.countOfWordsToReview, it.size)
        })

        binding.lifecycleOwner = this

        return binding.root
    }
}