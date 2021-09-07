package com.zhukofff.words

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.get
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
}