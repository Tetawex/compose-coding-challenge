package com.skoove.challenge.domain.di

import com.skoove.challenge.domain.usecase.GetAudioDetailUseCase
import com.skoove.challenge.domain.usecase.GetAudioListUseCase
import com.skoove.challenge.domain.usecase.SetFavoriteAudioUseCase
import com.skoove.challenge.domain.usecase.SetAudioRatingUseCase
import org.koin.dsl.module

object Modules {

    val common = module {
        // Add domain specific dependencies here
        factory {
            GetAudioListUseCase(
                get(),
                get(),
                get(),
            )
        }
        factory {
            GetAudioDetailUseCase(
                get(),
                get(),
                get(),
            )
        }
        factory {
            SetFavoriteAudioUseCase(get())
        }
        factory {
            SetAudioRatingUseCase(get())
        }
    }

}
