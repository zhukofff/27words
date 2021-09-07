package com.zhukofff.words

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

const val PREFERENCE_NAME = "27WORDS_DATA"

const val PREF_DICTIONARY = "PREF_DICTIONARY"

class PrefRepository(val context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_DICTIONARY, Context.MODE_PRIVATE)
    private val editor = pref.edit()
    private val gson = Gson()

    val latestWords: Flow<List<String>?> = flow {
        latestWords = getDictionary()
        emit(latestWords)
    } .flowOn(Dispatchers.IO)

    private fun String.put(long: Long) {
        editor.putLong(this, long)
        editor.commit()
    }

    private fun String.put(int: Int) {
        editor.putInt(this, int)
        editor.commit()
    }

    private fun String.put(string: String) {
        editor.putString(this, string)
        editor.commit()
    }


    private fun String.getString() = pref.getString(this, "")

    fun setDictionary(dict: ArrayList<String?>?) {
        PREF_DICTIONARY.put(gson.toJson(dict))
        latestWords
    }

    fun getDictionary() : List<String>? {
        PREF_DICTIONARY.getString().also {
            return if (it!!.isNotEmpty())
                gson.fromJson(
                    PREF_DICTIONARY.getString(),
                    ArrayList::class.java
                ) as? ArrayList<String>
            else null!!
        }
    }
}