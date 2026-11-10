package com.Qesnisu9.Madqis.core.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.Qesnisu9.Madqis.data.local.PreferencesStore
import com.Qesnisu9.Madqis.data.repository.ProgressRepositoryImpl
import com.Qesnisu9.Madqis.data.repository.SettingsRepositoryImpl

object ServiceLocator {

    private var viewModelFactory: ViewModelProvider.Factory? = null

    fun init(context: Context) {
        if (viewModelFactory != null) return
        val store = PreferencesStore(context.applicationContext)
        viewModelFactory = AppViewModelFactory(
            ProgressRepositoryImpl(store),
            SettingsRepositoryImpl(store)
        )
    }

    val factory: ViewModelProvider.Factory
        get() = requireNotNull(viewModelFactory) { NOT_READY }

    private const val NOT_READY = "ServiceLocator.init must run before the first screen"
}
