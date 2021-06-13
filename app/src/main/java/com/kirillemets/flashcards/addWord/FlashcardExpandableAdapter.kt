package com.kirillemets.flashcards.addWord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirillemets.flashcards.databinding.ItemWordDefinitionBinding

class FlashcardExpandableAdapter(
    private val callback: AddWordFragmentAdapter.AddWordFragmentAdapterCallback):
    RecyclerView.Adapter<FlashcardExpandableAdapter.ExpandableViewHolder>() {

    lateinit var searchResultCard: SearchResultCard
    var definitions: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder {
        return ExpandableViewHolder(
            ItemWordDefinitionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder, position: Int) {
        holder.bind(position, searchResultCard, callback)
    }

    override fun getItemCount(): Int = definitions.size

    class ExpandableViewHolder(private val binding: ItemWordDefinitionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            index: Int, searchResultCard: SearchResultCard,
            callback:AddWordFragmentAdapter.AddWordFragmentAdapterCallback
        ) {
            binding.definitionId = index
            binding.searchResult = searchResultCard

            binding.buttonAddWord.setOnClickListener {
                binding.buttonAddWord.isClickable = false
                callback.call(searchResultCard, index)
            }
        }
    }
}