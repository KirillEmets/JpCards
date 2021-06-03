package com.kirillemets.flashcards.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.databinding.FragmentReviewBinding
import com.kirillemets.flashcards.myDictionary.MyDictionaryFragmentViewModelFactory

class ReviewFragment : Fragment() {
    private lateinit var viewModel: ReviewFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val database = CardDatabase.getInstance(requireContext()).flashCardsDao()
        val binding = FragmentReviewBinding.inflate(inflater)

        viewModel = ViewModelProvider(
            this,
            MyDictionaryFragmentViewModelFactory(database)
        ).get(ReviewFragmentViewModel::class.java)

        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
        viewModel.delayHardMultiplier = preferenceManager.getFloat("hard_multiplier", 1f)
        viewModel.delayEasyMultiplier = preferenceManager.getFloat("easy_multiplier", 1f)

        val size = preferenceManager.getFloat("review_font_size", 30f).toInt()
        viewModel.fontSizeBig.value = (size * resources.displayMetrics.scaledDensity).toInt()
        viewModel.fontSizeSmall.value = ((size - 12) * resources.displayMetrics.scaledDensity).toInt()


        binding.viewModel = viewModel

        viewModel.reviewCards.observe(viewLifecycleOwner, {
            binding.countOfCardsTextView.text = resources.getString(R.string.countOfWordsToReview, it.size)
        })

        viewModel.currentCard.observe(viewLifecycleOwner, {
            binding.easyButtonDelay.text = resources.getQuantityString(R.plurals.daysToDelay, viewModel.getNewDelay(it.lastDelay, 1), viewModel.getNewDelay(it.lastDelay, 1))
            binding.hardButtonDelay.text = resources.getQuantityString(R.plurals.daysToDelay, viewModel.getNewDelay(it.lastDelay, 2), viewModel.getNewDelay(it.lastDelay, 2))
        })

        viewModel.onRunOutOfWords.observe(viewLifecycleOwner, {
            if(it) {
                viewModel.onRunOutOfWords.value = false
                findNavController().navigate(R.id.action_global_reviewFragment)
            }
        })

        binding.lifecycleOwner = this

        return binding.root
    }
}