package com.zhukofff.words

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.zhukofff.words.databinding.FragmentStudyBinding

class StudyFragment : Fragment() {

    private lateinit var binding : FragmentStudyBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var dict : ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyBinding.inflate(inflater, container, false)

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
        return binding.root
    }


    var dialogClickListener =
        DialogInterface.OnClickListener { dialog, which ->
            val intent : Intent
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    intent = Intent(activity, StudyActivityRus::class.java)
                    startActivity(intent)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    intent = Intent(activity, StudyActivity::class.java)
                    startActivity(intent)
                }
            }
        }


    private fun learnWords() {
        if (dict.size < 20) {
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