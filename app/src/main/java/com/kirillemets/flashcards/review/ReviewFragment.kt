package com.kirillemets.flashcards.review

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.database.DatabaseRepository
import com.kirillemets.flashcards.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {
    private lateinit var viewModel: ReviewFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        val database = CardDatabase.getInstance(requireContext()).flashCardsDao()
        val binding = FragmentReviewBinding.inflate(inflater)

        viewModel = ViewModelProvider(
            requireActivity(),
            ReviewFragmentViewModelFactory(DatabaseRepository(database))
        ).get(ReviewFragmentViewModel::class.java)

        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
        viewModel.apply {
            delayHardMultiplier = preferenceManager.getFloat("hard_multiplier", 1f)
            delayEasyMultiplier = preferenceManager.getFloat("easy_multiplier", 1f)

            val size = preferenceManager.getFloat("review_font_size", 30f).toInt()
            fontSizeBig.value = (size * resources.displayMetrics.scaledDensity).toInt()
            fontSizeSmall.value = ((size - 12) * resources.displayMetrics.scaledDensity).toInt()
        }

        binding.viewModel = viewModel

        viewModel.reviewCards.observe(viewLifecycleOwner, {
            binding.countOfCardsTextView.text =
                resources.getString(R.string.countOfWordsToReview, it.size)
        })

        viewModel.currentCard.observe(viewLifecycleOwner, {
            binding.easyButtonDelay.text = resources.getQuantityString(
                R.plurals.daysToDelay,
                viewModel.getNewDelay(it.lastDelay, 1),
                viewModel.getNewDelay(it.lastDelay, 1)
            )
            binding.hardButtonDelay.text = resources.getQuantityString(
                R.plurals.daysToDelay,
                viewModel.getNewDelay(it.lastDelay, 2),
                viewModel.getNewDelay(it.lastDelay, 2)
            )
        })

        viewModel.reviewStarted.observe(viewLifecycleOwner) { reviewStarted ->
            setMenuVisibility(reviewStarted)
        }

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.review_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete_card -> {
                viewModel.deleteCurrentCard()
                return true
            }
        }
        return false
    }
}