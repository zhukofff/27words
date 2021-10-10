package com.ubc.words.common

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ubc.words.databinding.MistakesItemBinding
import com.ubc.words.db.Mistakes

class MistakesAdapter(private val mistakes : ArrayList<Mistakes>) : BaseAdapter() {

    override fun getCount(): Int {
        return mistakes.size
    }

    override fun getItem(position: Int): Any {
        return mistakes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = MistakesItemBinding.inflate(layoutInflater, parent, false)

        val mistake = mistakes[position]
        binding.wordTranslate.setText(mistake.word + " - " + mistake.translate + " ")

        val spanBuilder = SpannableStringBuilder(mistake.mistake)
        val STRIKE_THROUGH_SPAN = StrikethroughSpan()
        spanBuilder.setSpan(
            STRIKE_THROUGH_SPAN,
            0,
            mistake.mistake.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.crossedOutMistake.setText(spanBuilder)
        return binding.root
    }
}