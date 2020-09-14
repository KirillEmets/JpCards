package com.kirillemets.flashcards.myDictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.database.FlashCard
import com.kirillemets.flashcards.databinding.ItemDictionaryFlashcardBinding

class MyDictionaryFragmentAdapter: RecyclerView.Adapter<MyDictionaryFragmentAdapter.MyDictionaryFragmentViewHolder>() {

    var currentTimeMillis: Long = 0

    var cards: List<FlashCard> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val checkedCards = mutableSetOf<Int>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyDictionaryFragmentViewHolder {
        val binding = ItemDictionaryFlashcardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val holder = MyDictionaryFragmentViewHolder(binding)

        binding.card.setOnLongClickListener { card ->
            (card as MaterialCardView).isChecked = !card.isChecked

            if (card.isChecked) checkedCards.add(holder.cardId)
            else checkedCards.remove(holder.cardId)
            true
        }

        binding.card.setOnClickListener { card ->
            if (checkedCards.size > 0) {
                (card as MaterialCardView).isChecked = !card.isChecked

                if (card.isChecked) checkedCards.add(holder.cardId)
                else checkedCards.remove(holder.cardId)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: MyDictionaryFragmentViewHolder, position: Int) {
        holder.bind(cards[position], checkedCards, currentTimeMillis)
    }

    override fun getItemCount(): Int = cards.size

    class MyDictionaryFragmentViewHolder(val binding: ItemDictionaryFlashcardBinding):
        RecyclerView.ViewHolder(binding.root) {
        var cardId: Int = 0
        fun bind(card: FlashCard, checkedCards: Set<Int>, currentTimeMillis: Long) {
            binding.flashCard = card
            val remainingTime = card.getRemainingTimes(currentTimeMillis)
            binding.daysRemaining.text =
                binding.root.resources.getString(R.string.daysRemainingText, remainingTime.first, remainingTime.second)
            cardId = card.cardId
            binding.card.isChecked = checkedCards.contains(cardId)
        }
    }
}