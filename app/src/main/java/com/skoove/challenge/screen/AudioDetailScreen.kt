package com.skoove.challenge.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.skoove.challenge.component.AudioDetailItem
import com.skoove.challenge.ui.MediaPlayerState
import com.skoove.challenge.viewmodel.AudioDetailViewModel
import com.skoove.challenge.viewmodel.AudioDetailViewState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AudioDetailScreen(
    audioUri: String,
) {
    val viewModel =
        koinViewModel<AudioDetailViewModel> { parametersOf(audioUri) }

    val state by viewModel.state.collectAsState(initial = AudioDetailViewState.Default)

    state.audioEntry?.let { audioEntry ->
        Column(Modifier.fillMaxSize()) {
            AudioDetailItem(
                audio = audioEntry.entry,
                mediaPlayer = viewModel,
                isFavorite = audioEntry.isFavorite,
                duration = audioEntry.entry.totalDurationMs,
                rating = audioEntry.rating,
                onStarClicked = viewModel::setRating,
                onFavoriteClicked = viewModel::setFavorite,
            )
        }
    }
}