package com.kirillemets.flashcards.review

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private val viewModel: ReviewFragmentViewModel by viewModels(ownerProducer = { requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        val binding = FragmentReviewBinding.inflate(inflater)

        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
        viewModel.apply {
            delayMissMultiplier = preferenceManager.getFloat("miss_multiplier", 0f)
            delayHardMultiplier = preferenceManager.getFloat("hard_multiplier", 1f)
            delayEasyMultiplier = preferenceManager.getFloat("easy_multiplier", 1f)
        }

        binding.viewModel = viewModel

        viewModel.currentCard.observe(viewLifecycleOwner, { card ->
            viewModel.getNewDelay(card.lastDelay, 0).let {
                binding.missButtonDelay.text = if(it == 0) "discard" else resources.getQuantityString(
                    R.plurals.daysToDelay, it, it
                )
            }
            viewModel.getNewDelay(card.lastDelay, 1).let {
                binding.easyButtonDelay.text = resources.getQuantityString(
                    R.plurals.daysToDelay, it, it
                )
            }
            viewModel.getNewDelay(card.lastDelay, 2).let {
                binding.hardButtonDelay.text = resources.getQuantityString(
                    R.plurals.daysToDelay, it, it
                )
            }
        })

        viewModel.onRunOutOfWords.observe(viewLifecycleOwner, {
            if(it) {
                findNavController().navigateUp()
                viewModel.onRunOutOfWords.value = false
            }
        })

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

