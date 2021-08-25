package com.zhukofff.words

import android.R
import android.text.Editable
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext
import com.zhukofff.words.StudyFragment
import org.json.JSONObject


class TranslateViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    val studyFragment = StudyFragment()
    val translatedWords = MutableLiveData<ArrayList<String>>()
    lateinit var translate : String

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
            translatedWords.postValue(translatedWordsArray)
        }
    }


    /*fun isPairOfWordsInDict(engWord: String, rusWord: String) : Boolean {
        for (i in 0 .. studyFragment.dict.size step 2) {
            if (studyFragment.dict.get(i).equals(engWord) && studyFragment.dict.get(i+1).equals(rusWord))
                return true
        }
        return false
    }

    fun addToDictionary(engWord: String, rusWord: String) {
        studyFragment.dict.add(engWord)
        studyFragment.dict.add(rusWord)

        with(studyFragment) { wordsAdapter!!.notifyDataSetChanged() }

    }*/
