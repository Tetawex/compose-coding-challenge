package com.skoove.challenge.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.skoove.challenge.domain.repository.AudioEntryRepository
import com.skoove.challenge.domain.usecase.GetAudioListUseCase
import com.skoove.challenge.domain.usecase.SetFavoriteAudioUseCase
import com.skoove.challenge.ui.MyApp
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.get

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider {
                MyApp()
            }
        }
    }
}
