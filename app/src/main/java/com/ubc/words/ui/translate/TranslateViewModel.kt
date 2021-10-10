        package com.ubc.words.ui.translate

import androidx.lifecycle.*
import com.ubc.words.db.Dictionary
import com.ubc.words.db.UserPreferencesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext


class TranslateViewModel(private val pref: UserPreferencesRepository) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val mutableTranslatedWords = MutableLiveData<List<String>?>()
    val translatedWords: LiveData<List<String>?> = mutableTranslatedWords
    lateinit var translate : String
    val dictionarySharedFlow = pref.userPreferencesSharedFlow
    lateinit var _dictionaryUiModel: Dictionary

    init {
         viewModelScope.launch {
             dictionarySharedFlow.collect {
                _dictionaryUiModel = it
             }
        }
    }

    fun setDictionary(word: String, translate: String) {
        val dict = mutableListOf("${word}", "${translate}")
        if (_dictionaryUiModel?.words != null) {
            for (i in 0 until _dictionaryUiModel!!.words!!.size)
                dict.add(_dictionaryUiModel!!.words!![i])
        }
        _dictionaryUiModel = Dictionary(dict)
        viewModelScope.launch {
            pref.setDictionary(_dictionaryUiModel!!.words)
        }
        // Log.v("translate", "${_dictionaryUiModel.words}")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }

    fun downloadHtml(vararg urls: String?) =
        launch {
            val translatedWordsArray = ArrayList<String>()
            translate = ""
            val url: URL = URL(urls[0])
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val input: InputStream = urlConnection.inputStream
            val reader: InputStreamReader = InputStreamReader(input)
            var data = reader.read()
            while (data != -1) {
                translate += data.toChar()
                data = reader.read()
            }
            val jsonObject = JSONObject(translate)
            val defArray = jsonObject.getJSONArray("def")
            for (i in 0 until defArray.length()) {
                val defObject = defArray.getJSONObject(i)
                val trArray = defObject.getJSONArray("tr")
                for (j in 0 until trArray.length()) {
                    val trArrayItem = trArray.getJSONObject(j)
                    translatedWordsArray.add(trArrayItem.getString("text"))
                }
            }
            // так как поток не main, а IO -- дёргаем колбек для отработки присваивания уже после
            // фонового потока
            mutableTranslatedWords.postValue(translatedWordsArray)
        }


    fun isPairOfWordsInDict(engWord: String, rusWord: String) : Boolean {
        try {
        // доставать информацию о размере словаря из sharedPreferences
            for (i in 0 until _dictionaryUiModel.words!!.size step 2) {
                if (_dictionaryUiModel.words!!.get(i).equals(engWord) && _dictionaryUiModel.words!!.get(i + 1).equals(rusWord))
                    return true
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}