package com.kirillemets.flashcards.android.ui.addWord

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirillemets.flashcards.shared.domain.model.DictionaryEntry
import com.kirillemets.flashcards.android.databinding.ItemSearchedFlashcardBinding

class AddWordFragmentAdapter(private val callback: AddWordFragmentAdapterCallback): RecyclerView.Adapter<AddWordFragmentAdapter.FlashCardViewHolder>() {
    var dictionaryEntries: List<DictionaryEntry> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashCardViewHolder {
        val binding = ItemSearchedFlashcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.expandable.adapter = FlashcardExpandableAdapter(callback)

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
        holder.bind(dictionaryEntries[position])
    }

    override fun getItemCount(): Int = dictionaryEntries.size

    class FlashCardViewHolder(
        private val binding: ItemSearchedFlashcardBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        var visible: Boolean = false

        fun bind(flashCard: DictionaryEntry) {
            binding.resultCard = flashCard
            visible = false
            val expandableAdapter = (binding.expandable.adapter as FlashcardExpandableAdapter)
            expandableAdapter.definitions = flashCard.englishMeanings
            expandableAdapter.dictionaryEntry = flashCard
            binding.expandable.visibility = View.GONE
        }
    }

    class AddWordFragmentAdapterCallback(private val cb: (DictionaryEntry, Int) -> Unit) {
        fun call(card: DictionaryEntry, definitionId: Int) = cb(card, definitionId)
    }
}