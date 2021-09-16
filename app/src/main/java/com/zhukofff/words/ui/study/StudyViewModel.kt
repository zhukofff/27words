package com.zhukofff.words.ui.study

import androidx.lifecycle.*
import com.zhukofff.words.db.PrefRepository


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
        fetchDictionaryData()
    }

    fun fetchDictionaryData() {
/*        viewModelScope.launch {
            pref.latestWords.collect {  latestWords ->
                mDictionary.value = latestWords
            }
        }*/
        mDictionary.value = pref.getDictionary()
    }


}