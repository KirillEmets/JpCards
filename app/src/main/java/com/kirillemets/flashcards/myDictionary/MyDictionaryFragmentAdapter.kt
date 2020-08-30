package com.kirillemets.flashcards.myDictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirillemets.flashcards.database.FlashCard
import com.kirillemets.flashcards.databinding.FragmentMyDictionaryBinding
import com.kirillemets.flashcards.databinding.ItemDictionaryFlashcardBinding

class MyDictionaryFragmentAdapter: RecyclerView.Adapter<MyDictionaryFragmentAdapter.MyDictionaryFragmentViewHolder>() {

    var cards: List<FlashCard> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyDictionaryFragmentViewHolder {
        val binding = ItemDictionaryFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyDictionaryFragmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyDictionaryFragmentViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    class MyDictionaryFragmentViewHolder(private val binding: ItemDictionaryFlashcardBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: FlashCard) {
            binding.flashCard = card
        }
    }
}