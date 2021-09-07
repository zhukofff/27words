package com.zhukofff.words

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext


class TranslateViewModel(private val prefRepository: PrefRepository) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val mutableTranslatedWords = MutableLiveData<ArrayList<String?>>()
    val translatedWords: LiveData<ArrayList<String?>> = mutableTranslatedWords
    lateinit var translate : String
    private val list = ArrayList<String?>()

    private val mutableDictionary = MutableLiveData<ArrayList<String?>>()
    val dictionary : LiveData<ArrayList<String?>> = mutableDictionary


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + CoroutineExceptionHandler { _, e -> throw e }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }

    fun downloadHtml(vararg urls: String?) =
        launch {
            val translatedWordsArray = ArrayList<String?>()
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
        val dict  = prefRepository.getDictionary()
            for (i in 0 until dict!!.size step 2) {
                if (dict.get(i).equals(engWord) && dict.get(i + 1).equals(rusWord))
                    return true
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun addToDictionary(engWord: String, rusWord: String) {
        // сразу добавлять в sharedPreference
        list.add(engWord)
        list.add(rusWord)
        mutableDictionary.value = list
        prefRepository.setDictionary(mutableDictionary.value)
    }
}