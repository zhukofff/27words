package com.zhukofff.words.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhukofff.words.db.PrefRepository
import kotlin.random.Random


class GameViewModel(private val pref: PrefRepository) : ViewModel() {

    // pseudorandom calculating
    // get words from repository
    private val mDictionary = MutableLiveData<List<String>>()
    private val dictionary : LiveData<List<String>> = mDictionary
    private val mDisplayWords = MutableLiveData<ArrayList<String?>>()
    val displayWords : LiveData<ArrayList<String?>> = mDisplayWords
    private val lengthLearningWords = 10
    private val indexesOfLearningWords = ArrayList<Int>()
    val mistakes = ArrayList<String>()
    private val mDisplayScore = MutableLiveData<Int>()
    val displayScore : LiveData<Int> = mDisplayScore
    private var score = 0

    init {
        fetchDictionaryData()
    }

    private fun fetchDictionaryData() {
        /*viewModelScope.launch {
            pref.latestWords.collect { dictionary ->
                mDictionary.value = dictionary
            }
        }*/
        mDictionary.value = pref.getDictionary()
    }

    fun generateLearningWords(language: String) {

        for (i in 0 until dictionary.value!!.size)
            Log.v("words", "${dictionary.value?.get(i)}")

        // generate empty array
        for (i in 0 until 10)
            indexesOfLearningWords.add(-1)

        var i = 0
        while (i < indexesOfLearningWords.size) {
            val index = Random.nextInt(dictionary.value!!.size)
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
        else if (number % 2 != 0 && number != (dictionary.value?.size?.minus(1)))
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
            listFakeWords.add(dictionary.value?.get(makeOdd(Random.nextInt(dictionary.value!!.size))))
            listFakeWords.add(dictionary.value?.get(makeOdd(Random.nextInt(dictionary.value!!.size))))
            listFakeWords.add(dictionary.value?.get(indexesOfLearningWords[currentIndex] + 1))
        } else {
            listFakeWords.add(dictionary.value?.get(makeEven(Random.nextInt(dictionary.value!!.size))))
            listFakeWords.add(dictionary.value?.get(makeEven(Random.nextInt(dictionary.value!!.size))))
            listFakeWords.add(dictionary.value?.get(indexesOfLearningWords[currentIndex] - 1))
        }

        listFakeWords.add(dictionary.value?.get(indexesOfLearningWords[currentIndex]))

        var i = 0
        while (i < 2) {
            if (language == "english")
                listFakeWords[i] = dictionary.value?.get(makeOdd((Random.nextInt(dictionary.value!!.size))))
            else
                listFakeWords[i] = dictionary.value?.get(makeEven((Random.nextInt(dictionary.value!!.size))))

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
        if (language == "english" && dictionary.value?.get(indexesOfLearningWords[step] + 1) != userChoice) {
            mistakes.add(dictionary.value?.get(indexesOfLearningWords[step]) + " - " + userChoice)
        } else if (language == "russian" && dictionary.value?.get(indexesOfLearningWords[step] - 1) != userChoice){
            mistakes.add(dictionary.value?.get(indexesOfLearningWords[step]) + " - " + userChoice)
        } else {
            score++
            mDisplayScore.value = score
        }
    }
}

