package com.kirillemets.flashcards.addWord

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirillemets.flashcards.databinding.ItemWordDefinitionBinding

class FlashcardExpandableAdapter(): RecyclerView.Adapter<FlashcardExpandableAdapter.ExpandableViewHolder>() {

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
        holder.bind(definitions[position], position)
    }

    override fun getItemCount(): Int = definitions.size

    class ExpandableViewHolder(private val binding: ItemWordDefinitionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(definition: String, index: Int) {
            binding.definition = (index + 1).toString() + ". " + definition
        }
    }
}