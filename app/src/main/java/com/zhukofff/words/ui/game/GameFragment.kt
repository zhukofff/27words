package com.zhukofff.words.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zhukofff.words.R
import com.zhukofff.words.databinding.FragmentGameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class GameFragment() : Fragment(){

    private val gameViewModel by viewModel<GameViewModel>()
    private var step = 0
    private lateinit var binding : FragmentGameBinding
    private val args: GameFragmentArgs by navArgs()
    private val language by lazy {
        args.language
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        gameViewModel.generateLearningWords(language)
        gameViewModel.game(0, language)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wordsObserver = Observer<ArrayList<String?>> { words ->
            binding.word.setText(words[3])
            when (Random.nextInt(3)) {
                0 -> {
                    binding.translate1.setText(words[2])
                    binding.translate2.setText(words[1])
                    binding.translate3.setText(words[0])
                }
                1 -> {
                    binding.translate1.setText(words[0])
                    binding.translate2.setText(words[2])
                    binding.translate3.setText(words[1])
                }
                2 -> {
                    binding.translate1.setText(words[0])
                    binding.translate2.setText(words[1])
                    binding.translate3.setText(words[2])
                }
            }
        }

        gameViewModel.displayWords.observe(viewLifecycleOwner, wordsObserver)
        // how to insert data to binding elements?
        // i think some calculate need to be in ViewModel

        val scoreObserver = Observer<Int> { score ->
            binding.score.setText("score: $score / 10")
        }
        gameViewModel.displayScore.observe(viewLifecycleOwner, scoreObserver)

        binding.translate1.setOnClickListener {
            if (step < 10)
                gameViewModel.checkRightOrWrong(step, binding.translate1.text.toString(), language)
            getNextWords()
        }
        binding.translate2.setOnClickListener{
            if (step < 10)
                gameViewModel.checkRightOrWrong(step, binding.translate2.text.toString(), language)
            getNextWords()
        }
        binding.translate3.setOnClickListener {
            if (step < 10)
                gameViewModel.checkRightOrWrong(step, binding.translate3.text.toString(), language)
            getNextWords()
        }
        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_game_to_study)
        }
    }

    private fun getNextWords() {
        if (step == 9) {
            binding.translate1.visibility = View.GONE
            binding.translate2.visibility = View.GONE
            binding.translate3.visibility = View.GONE
            binding.word.setText("ошибки")

            val mistakesAdapter = ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1,
                gameViewModel.mistakes
            )
            binding.mistakes.adapter = mistakesAdapter
        } else {
            step++
            gameViewModel.game(step, language)
        }
    }
}
