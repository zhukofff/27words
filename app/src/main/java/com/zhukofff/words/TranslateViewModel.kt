package com.zhukofff.words

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

class TranslateViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
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

}