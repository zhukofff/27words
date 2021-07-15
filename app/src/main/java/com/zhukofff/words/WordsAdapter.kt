package com.zhukofff.words
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhukofff.words.databinding.WordItemBinding
import java.util.*
import kotlin.collections.ArrayList

class WordsAdapter(dict: ArrayList<String>) : RecyclerView.Adapter<WordsAdapter.WordViewHolder>() {

    private var dictWithPairOfWords = ArrayList<String>(dict.size/2)

    init {
        for (i in 0..dictWithPairOfWords.size) {
            dictWithPairOfWords.add(dict.get(i*2) + " - " + dict.get(i*2+1))
        }
        Collections.sort(dictWithPairOfWords)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(dictWithPairOfWords.get(position))
    }

    override fun getItemCount(): Int {
        return dictWithPairOfWords.size
    }

    class WordViewHolder(private val binding: WordItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(word: String) {
            binding.textWordItem.text = word as Editable
        }
        companion object {
            fun from(parent: ViewGroup) : WordViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WordItemBinding.inflate(layoutInflater, parent, false)
                return WordViewHolder(binding)
            }
        }
    }
}

// TODO: using ROOM implement callback
/*class WordCallback: DiffUtil.ItemCallback<ArrayList<String>>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        oldItem == newItem
    }
}*/

