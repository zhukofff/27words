package com.ubc.words.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ubc.words.databinding.WordItemBinding
import java.util.*
import kotlin.collections.ArrayList

class WordsAdapter(private val dict: List<String>) : RecyclerView.Adapter<WordsAdapter.WordViewHolder>() {

    private var dictWords = ArrayList<String>()
    private var dictTranslatedWords = ArrayList<String>()

    init {
        for (i in 0 until dict.size/2) {
                dictWords.add(dict.get(i*2))
                dictTranslatedWords.add(" - " + dict.get(i*2+1))
        }
        // Collections.sort(dictWithPairOfWords)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(dictWords.get(position), dictTranslatedWords.get(position))
    }

    override fun getItemCount(): Int {
        return dictWords.size
    }


    class WordViewHolder(private val binding: WordItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(word: String, translate: String) {
            binding.textWordItem.setText(word)
            binding.translate.setText(translate)
        }
        companion object {
            fun from(parent: ViewGroup) : WordViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WordItemBinding.inflate(layoutInflater, parent, false)
                return WordViewHolder(binding)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArrayList<String>> =
            object : DiffUtil.ItemCallback<ArrayList<String>>() {

                override fun areItemsTheSame(
                    oldItem: ArrayList<String>,
                    newItem: ArrayList<String>
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun areContentsTheSame(
                    oldItem: ArrayList<String>,
                    newItem: ArrayList<String>
                ): Boolean {
                    TODO("Not yet implemented")
                }
            }
    }
}


