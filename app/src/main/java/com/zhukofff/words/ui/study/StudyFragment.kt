package com.zhukofff.words.ui.study

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.savedstate.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zhukofff.words.common.RecyclerItemClickListener
import com.zhukofff.words.common.WordsAdapter
import com.zhukofff.words.databinding.FragmentStudyBinding
import com.zhukofff.words.db.Dictionary
import com.zhukofff.words.ui.MainFragmentDirections
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

const val TAG = "StudyFragment"
class StudyFragment : Fragment() {

    private lateinit var binding : FragmentStudyBinding
    private val studyViewModel by viewModel<StudyViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val dictionaryObserver = Observer<Dictionary?> { dict ->
            if (dict != null) {
                val wordsAdapter = dict.words?.let { WordsAdapter(it) }
                binding.rvStudy.adapter = wordsAdapter
            }
        }

        studyViewModel.dictionary.observe(viewLifecycleOwner, dictionaryObserver)*/

        lifecycleScope.launchWhenStarted {
            studyViewModel.userPreferencesFlow.collect { dict ->
                if (dict != null) {
                    val wordsAdapter = dict.words?.let {
                        WordsAdapter(it)
                    }
                    binding.rvStudy.adapter = wordsAdapter
                }
            }
        }
        binding.buttonStudy.setOnClickListener {
            learnWords()
        }

        binding.buttonHide.setOnClickListener {
            if (binding.rvStudy.visibility == View.VISIBLE) {
                binding.rvStudy.visibility = View.GONE
                binding.buttonStudy.visibility = View.GONE
            } else {
                binding.rvStudy.visibility = View.VISIBLE
                binding.buttonStudy.visibility = View.VISIBLE
            }
        }

        binding.rvStudy.addOnItemTouchListener(RecyclerItemClickListener(requireContext(),binding.rvStudy, object:
            RecyclerItemClickListener.OnItemClickListener
        {
            override fun onLongItemClick(view: View, position: Int){
                AlertDialog.Builder(requireActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Word Deletion")
                    .setMessage("Are you sure you want delete this word?")
                    .setPositiveButton("Yes", object: DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            val word = (binding.rvStudy.findViewHolderForAdapterPosition(position)?.
                            itemView?.findViewById<TextView>(com.zhukofff.words.R.id.text_word_item))!!
                                .text.toString()
                            studyViewModel.deleteFromDictionary(word)
                        }
                    }).setNegativeButton("No", null)
                    .show()
            }
        }))
    }

    var dialogClickListener =
        DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val action = MainFragmentDirections.actionStudyToGame("russian")
                    findNavController().navigate(action)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    val action = MainFragmentDirections.actionStudyToGame("english")
                    findNavController().navigate(action)
                }
            }
        }

    private fun learnWords() {
        if (studyViewModel.dictionary.words?.size!! < 20) {
            Toast.makeText(
                requireContext(),
                "Добавьте хотя бы 10 слов...",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Which language you want to study?")
                .setPositiveButton("Russian", dialogClickListener)
                .setNegativeButton("English", dialogClickListener)
                .show()
        }
    }
}
