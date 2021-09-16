package com.zhukofff.words

import com.zhukofff.words.db.PrefRepository
import com.zhukofff.words.ui.game.GameViewModel
import com.zhukofff.words.ui.study.StudyViewModel
import com.zhukofff.words.ui.translate.TranslateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single {
        PrefRepository(get())
    }
}

val viewModelModule = module {
    viewModel {
        StudyViewModel(get())
    }
    viewModel {
        TranslateViewModel(get())
    }
    viewModel {
        GameViewModel(get())
    }
}