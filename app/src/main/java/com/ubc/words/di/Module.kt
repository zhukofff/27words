package com.ubc.words


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ubc.words.db.UserPreferencesRepository
import com.ubc.words.ui.game.GameViewModel
import com.ubc.words.ui.study.StudyViewModel
import com.ubc.words.ui.translate.TranslateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

val dataStoreModule = module {

    fun provideDataStore(context: Context) : DataStore<Preferences> =
        context.dataStore

    single {
        provideDataStore(get())
    }
}

val repositoryModule = module {
    single {
        UserPreferencesRepository(get(), get())
    }

    factory { SupervisorJob() }
    factory { CoroutineScope(Dispatchers.Default)}
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