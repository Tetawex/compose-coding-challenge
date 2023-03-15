package com.skoove.challenge.di

import android.app.Application
import com.skoove.challenge.viewmodel.AudioDetailViewModel
import com.skoove.challenge.viewmodel.AudioListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

/// This modules main DI configuration
object Modules {

    /// Main trigger to initialize this module's dependencies
    fun init(context: Application) {
        startKoin {
            androidContext(context)
            modules(
                com.skoove.challenge.data.di.Modules.common,
                com.skoove.challenge.domain.di.Modules.common,
                main
            )
        }
    }

    /// Main declaration for this modules dependencies
    private val main = module {
        viewModel {
            AudioListViewModel(
                getAudioListUseCase = get(),
                setFavoriteAudioUseCase = get()
            )
        }
        viewModel { parameters ->
            AudioDetailViewModel(audioUri = parameters.get(), get(), get(), get())
        }
    }
}
