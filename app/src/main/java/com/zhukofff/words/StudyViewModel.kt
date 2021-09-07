package com.zhukofff.words

import androidx.lifecycle.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class StudyViewModel(private val pref: PrefRepository) : ViewModel() {

    // для чего я создал StudyViewModel?
    /*
    Я столкнулся с проблемой передачи данных из одного фрагмента в другой.
    Алгоритм такой:
     TranslateFragment --> TranslateViewModel --> sharedPreferences --> StudyViewModel --> StudyFragment

     При загрузке нового экрана получаем данные из sharedPreference и уведомляем об этом фрагмент
     */


    private val mDictionary = MutableLiveData<List<String>?>()
    val dictionary = mDictionary

    init {
        viewModelScope.launch {
            pref.latestWords.collect {  latestWords ->
                mDictionary.value = latestWords
            }
        }
    }


}