package com.kirillemets.flashcards.myDictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.R
import com.kirillemets.flashcards.databinding.FragmentMyDictionaryBinding


class MyDictionaryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val viewModel: MyDictionaryFragmentViewModel by lazy {
        ViewModelProvider(this).get(MyDictionaryFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyDictionaryBinding.inflate(layoutInflater)

        return binding.root
    }

}