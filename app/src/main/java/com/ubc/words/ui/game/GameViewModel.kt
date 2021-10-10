package com.ubc.words.ui.game

import android.util.Log
import androidx.lifecycle.*
import com.ubc.words.db.Dictionary
import com.ubc.words.db.Mistakes
import com.ubc.words.db.UserPreferencesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random


class GameViewModel(private val pref: UserPreferencesRepository) : ViewModel() {

    // pseudorandom calculating
    // get words from repository
    private val userPreferencesFlow = pref.userPreferencesSharedFlow
    private lateinit var dictionary: Dictionary

    init {
        viewModelScope.launch {
            userPreferencesFlow.collect {
                dictionary = it
            }
        }
    }

    private val mDisplayWords = MutableLiveData<ArrayList<String?>>()
    val displayWords : LiveData<ArrayList<String?>> = mDisplayWords
    private val lengthLearningWords = 10
    private val indexesOfLearningWords = ArrayList<Int>()
    val mistakes = ArrayList<Mistakes>()
    private val mDisplayScore = MutableLiveData<Int>()
    val displayScore : LiveData<Int> = mDisplayScore
    private var score = 0

    fun generateLearningWords(language: String) {

        for (i in 0 until dictionary.words!!.size)
            Log.v("words", "${dictionary.words!!.get(i)}")

        // generate empty array
        for (i in 0 until 10)
            indexesOfLearningWords.add(-1)

        var i = 0
        while (i < indexesOfLearningWords.size) {
            val index = Random.nextInt(dictionary.words!!.size)
            if (language == "english")
                indexesOfLearningWords[i] = makeEven(index)
            else
                indexesOfLearningWords[i] = makeOdd(index)
            // check for a match in array
            for (j in 0 until indexesOfLearningWords.size) {
                if (indexesOfLearningWords[j] == indexesOfLearningWords[i] && i != j) {
                    i--
                    break
                }
            }
            i++
        }

        for (i in 0 until indexesOfLearningWords.size)
            Log.v("createdIndex", "${indexesOfLearningWords[i]}")
    }

    private fun makeEven(number: Int) : Int {
        var returnNumber = 0
        if (number % 2 == 0)
            returnNumber = number
        else if (number % 2 != 0 && number != (dictionary.words!!.size?.minus(1)))
            returnNumber = number.plus(1)
        else
            returnNumber = number.minus(1)
        return returnNumber
    }

    /*
    1. Генерируем два рандомных значения
    2. Делаем их нечётными
    3. Проверяем на эквивалентность с ответом и эквивалентность между друг другом, если какое - то значение одинаково другому, то исправляем
     */

    private fun makeOdd(number: Int) : Int {
        var returnNumber = 0
        if (number % 2 == 1)
            returnNumber = number
        else
            returnNumber = number.plus(1)
        return returnNumber
    }

    private fun generateFakeWords(currentIndex: Int, language: String) : ArrayList<String?> {
        val listFakeWords = ArrayList<String?>()

        if (language == "english") {
            listFakeWords.add(dictionary.words!!.get(makeOdd(Random.nextInt(dictionary.words!!.size))))
            listFakeWords.add(dictionary.words!!.get(makeOdd(Random.nextInt(dictionary.words!!.size))))
            listFakeWords.add(dictionary.words!!.get(indexesOfLearningWords[currentIndex] + 1))
        } else {!!
            listFakeWords.add(dictionary.words!!.get(makeEven(Random.nextInt(dictionary.words!!.size))))
            listFakeWords.add(dictionary.words!!.get(makeEven(Random.nextInt(dictionary.words!!.size))))
            listFakeWords.add(dictionary.words!!.get(indexesOfLearningWords[currentIndex] - 1))
        }

        listFakeWords.add(dictionary.words!!.get(indexesOfLearningWords[currentIndex]))

        var i = 0
        while (i < 2) {
            if (language == "english")
                listFakeWords[i] = dictionary.words!!.get(
                    makeOdd((Random.nextInt(dictionary.words!!.size))))
            else
                listFakeWords[i] = dictionary.words!!.get(
                    makeEven((Random.nextInt(dictionary.words!!.size))))

            for (j in 0 until 3)
                if (listFakeWords[i] == listFakeWords[j] && i != j) {
                    i--
                    break
                }
            i++
        }

        for (el in listFakeWords)
            Log.v("fake words and answers", "$el")
        return listFakeWords
    }

    fun game(step: Int, language: String) {
        mDisplayWords.value = generateFakeWords(step, language)
    }

    fun checkRightOrWrong(step: Int, userChoice: String, language: String) {
        if (language == "english" && dictionary.words!!.get(indexesOfLearningWords[step] + 1) != userChoice) {
            mistakes.add(Mistakes(
                dictionary.words!!.get(indexesOfLearningWords[step]),
                dictionary.words!!.get(indexesOfLearningWords[step] + 1),
                userChoice))
        } else if (language == "russian" && dictionary.words!!.get(indexesOfLearningWords[step] - 1) != userChoice){
            mistakes.add(Mistakes(
                dictionary.words!!.get(indexesOfLearningWords[step]),
                dictionary.words!!.get(indexesOfLearningWords[step] - 1),
                userChoice
            ))
        } else {
            score++
            mDisplayScore.value = score
        }
    }
}

