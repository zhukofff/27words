package com.zhukofff.words.ui.study

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zhukofff.words.common.WordsAdapter
import com.zhukofff.words.databinding.FragmentStudyBinding
import com.zhukofff.words.ui.MainFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        studyViewModel.fetchDictionaryData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dictionaryObserver = Observer<List<String>?> { dict ->
            if (dict != null) {
                val wordsAdapter = WordsAdapter(dict)
                binding.rvStudy.adapter = wordsAdapter
            }
        }

        studyViewModel.dictionary.observe(viewLifecycleOwner, dictionaryObserver)
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
        if (studyViewModel.dictionary.value!!.size < 20) {
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
