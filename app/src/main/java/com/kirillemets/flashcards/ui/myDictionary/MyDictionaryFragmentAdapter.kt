package com.kirillemets.flashcards.ui.myDictionary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.databinding.ItemDictionaryFlashcardBinding

class MyDictionaryFragmentAdapter(private val onItemClick: (itemId: Int) -> Unit) :
    RecyclerView.Adapter<MyDictionaryFragmentAdapter.MyDictionaryFragmentViewHolder>() {

    var currentTimeMillis: Long = 0

    var notes: List<NoteUIState> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val checkedCards = mutableSetOf<Int>()

    fun uncheckAllCards() {
        checkedCards.clear()
    }

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

        binding.ttsImageButton.setOnClickListener {
            onItemClick(holder.cardId)
        }

        return holder
    }

    override fun onBindViewHolder(holder: MyDictionaryFragmentViewHolder, position: Int) {
        holder.bind(notes[position], checkedCards)
    }

    override fun getItemCount(): Int = notes.size

    class MyDictionaryFragmentViewHolder(private val binding: ItemDictionaryFlashcardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var cardId: Int = 0

        @SuppressLint("SetTextI18n")
        fun bind(card: NoteUIState, checkedCards: Set<Int>) {
            binding.flashCard = card
            binding.daysRemaining.text = binding.root.resources.getString(
                R.string.progressInDays,
                card.lastDelay,
                card.lastDelayReversed
            )
            cardId = card.noteId
            binding.card.isChecked = checkedCards.contains(cardId)
        }
    }
}