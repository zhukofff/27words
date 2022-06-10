    package com.ubc.words.db

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.io.StringReader

private object PreferencesKeys {
    val DICTIONARY = stringPreferencesKey("dictionary")
}

class UserPreferencesRepository(
    private val userPreferencesStore: DataStore<Preferences>
) {

    private val gson = GsonBuilder().setLenient().create()

    val userPreferencesFlow: Flow<Dictionary> = userPreferencesStore.data
        .catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }
        .map { preferences ->
            val words = preferences[PreferencesKeys.DICTIONARY]
            if (words == null)
                Dictionary(mutableListOf("Hello", "привет"))
            else
                Dictionary(transformDictionary(words))
        }


    suspend fun setDictionary(dict: MutableList<String>?) {
        userPreferencesStore.edit { preferences ->
            preferences[PreferencesKeys.DICTIONARY] = gson.toJson(dict)
            Log.v("preferences", "${preferences[PreferencesKeys.DICTIONARY]}")
        }
    }

    private fun transformDictionary(dict: String?) : MutableList<String>? {
        dict.also {
             return if (it!!.isNotEmpty()) {
                val reader = StringReader(it)
                val jsonReader = JsonReader(reader)
                gson.fromJson(
                    jsonReader,
                    ArrayList::class.java
                ) as MutableList<String>?
            }
            else null
        }
    }
}
