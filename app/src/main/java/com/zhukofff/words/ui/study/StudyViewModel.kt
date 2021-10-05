package com.zhukofff.words.ui.study

import android.util.Log
import androidx.lifecycle.*
import com.zhukofff.words.db.Dictionary
import com.zhukofff.words.db.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class StudyViewModel(private val pref: UserPreferencesRepository) : ViewModel() {

    val userPreferencesFlow = pref.userPreferencesSharedFlow

    lateinit var dictionary: Dictionary

    init {
        viewModelScope.launch {
            userPreferencesFlow.collect {  dictionary = it }
        }
    }

    fun deleteFromDictionary(word: String) {
        for (i in 0 until dictionary.words!!.size) {
            if (dictionary.words!!.get(i).equals(word)) {
                dictionary.words!!.removeAt(i)
                dictionary.words!!.removeAt(i)
                    break
            }
        }
        viewModelScope.launch {
            pref.setDictionary(dictionary.words!!)
        }
        Log.v("study", "${dictionary.words}")
    }
}