package com.skoove.challenge.viewmodel

import com.skoove.challenge.domain.entity.ListAudioEntry
import com.skoove.challenge.domain.usecase.GetAudioDetailUseCase
import com.skoove.challenge.domain.usecase.SetAudioRatingUseCase
import com.skoove.challenge.domain.usecase.SetFavoriteAudioUseCase
import com.skoove.challenge.ui.MediaPlayerController
import com.skoove.challenge.ui.MediaPlayerState
import kotlinx.coroutines.flow.*

data class AudioDetailViewState(
    val mediaPlayerState: MediaPlayerState,
    val audioEntry: ListAudioEntry?
) {
    companion object {
        val Default =
            AudioDetailViewState(mediaPlayerState = MediaPlayerState.Initialized, audioEntry = null)
    }
}

class AudioDetailViewModel(
    val audioUri: String,
    private val setAudioRatingUseCase: SetAudioRatingUseCase,
    private val setFavoriteAudioUseCase: SetFavoriteAudioUseCase,
    getAudioDetailUseCase: GetAudioDetailUseCase
) : MediaPlayerController() {

    val state: Flow<AudioDetailViewState> =
        mediaPlayerState.combine(getAudioDetailUseCase.getAudioDetail(audioUri))
        { mediaPlayerState, audioEntry ->
            AudioDetailViewState(mediaPlayerState = mediaPlayerState, audioEntry = audioEntry)
        }

    init {
        audioSelected(audioUri)
    }

    fun setFavorite(isFavorite: Boolean) {
        setFavoriteAudioUseCase(audioUri, isFavorite)
    }

    fun setRating(newValue: Int) {
        setAudioRatingUseCase(audioUri, newValue)
    }

    override fun onCleared() {
        super.releaseMediaPlayer()
    }
}