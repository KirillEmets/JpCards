package com.kirillemets.flashcards.addWord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirillemets.flashcards.databinding.ItemSearchedFlashcardBinding

class AddWordFragmentAdapter: RecyclerView.Adapter<AddWordFragmentAdapter.FlashCardViewHolder>() {
    var searchResultCards: List<SearchResultCard> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashCardViewHolder {
        val binding = ItemSearchedFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.expandable.adapter = FlashcardExpandableAdapter()
        val holder = FlashCardViewHolder(binding)
        binding.card.setOnClickListener {
            if(holder.visible) {
                binding.expandable.visibility = View.GONE
            }
            else {
                binding.expandable.visibility = View.VISIBLE
            }
            holder.visible = !holder.visible
        }

        return holder
    }

    override fun onBindViewHolder(holder: FlashCardViewHolder, position: Int) {
        holder.bind(searchResultCards[position])
    }

    override fun getItemCount(): Int = searchResultCards.size

    class FlashCardViewHolder(private val binding: ItemSearchedFlashcardBinding): RecyclerView.ViewHolder(binding.root) {

        var visible: Boolean = false

        fun bind(flashCard: SearchResultCard) {
            binding.flashCard = flashCard
            (binding.expandable.adapter as FlashcardExpandableAdapter).definitions = flashCard.englishMeanings
            binding.expandable.visibility = View.GONE
        }
    }
}