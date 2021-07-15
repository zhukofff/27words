package com.zhukofff.words

import android.text.Editable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext
import com.zhukofff.words.StudyFragment

class TranslateViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    val studyFragment = StudyFragment()
    var translatedWords : String = ""

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, e -> throw e }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }


    fun downloadHtml(vararg urls: String?)  = launch {
        translatedWords = ""
        val url: URL = URL(urls[0])
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val input: InputStream = urlConnection.inputStream
        val reader: InputStreamReader = InputStreamReader(input)
        var data = reader.read()
        while (data != -1) {
            translatedWords += data
            data = reader.read()
        }

    }

    fun isPairOfWordsInDict(engWord: String, rusWord: String) : Boolean {
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

    }

}