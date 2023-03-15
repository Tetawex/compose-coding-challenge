package com.skoove.challenge.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Media player controller wrapped as a ViewModel exposing its current state
 */
abstract class MediaPlayerController : ViewModel() {
    // object of media player
    private val mediaPlayer = MediaPlayer()

    // sealed class for handling different media player states
    private val _mediaPlayerState =
        MutableStateFlow<MediaPlayerState>(MediaPlayerState.Initializing)

    // Media player attributes
    private val attributes = AudioAttributes
        .Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    private val isProgressSliderDragged = MutableStateFlow(false)

    val progressSliderValue = MutableStateFlow(getPlayerPosition())
    val mediaPlayerState = _mediaPlayerState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                // Sync slider progress with the media player every 0.5s
                // if the audio is playing and the slider is not dragged at the moment
                while (true) {
                    delay(500)
                    if (mediaPlayerState.value is MediaPlayerState.Started && !isProgressSliderDragged.value) {
                        progressSliderValue.value = getPlayerPosition()
                        Log.e("getPlayerPosition()", getPlayerPosition().toString())
                    }
                }
            }
        }
    }

    /**
     * Media player click handler
     */
    fun audioSelected(url: String) {
        when (mediaPlayerState.value) {
            MediaPlayerState.Started -> pauseMediaPlayer()
            MediaPlayerState.Paused, MediaPlayerState.Initialized, MediaPlayerState.Finished -> startMediaPlayer()
            else -> {
                initializeMediaPlayer(url)
            }
        }
    }

    /**
     * Release media player
     */
    fun releaseMediaPlayer() = runIfInitialized {
        mediaPlayer.stop()
        mediaPlayer.release()
        _mediaPlayerState.update { MediaPlayerState.Initializing }

    }

    fun dragProgressSlider(sliderValue: Float) = runIfInitialized {
        isProgressSliderDragged.value = true
        progressSliderValue.value = sliderValue
        pauseMediaPlayer()
    }

    fun releaseProgressSlider() {
        isProgressSliderDragged.value = false
        changeAudioProgress(progressSliderValue.value)
        startMediaPlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }

    private fun runIfInitialized(block: () -> Unit) {
        when (mediaPlayerState.value) {
            MediaPlayerState.Initializing -> Unit
            else -> {
                block()
            }
        }
    }

    private fun getPlayerPosition(): Float {
        return mediaPlayer.currentPosition.toFloat()
    }

    /**
     * Initialize media player with given url
     */
    private fun initializeMediaPlayer(url: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    mediaPlayer.setAudioAttributes(attributes)
                    mediaPlayer.setDataSource(url)
                    mediaPlayer.prepare()
                    mediaPlayer.seekTo(0)

                    withContext(Dispatchers.Main) {
                        _mediaPlayerState.update { MediaPlayerState.Initialized }
                        startMediaPlayer()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Start media player
     */
    private fun startMediaPlayer() {
        try {
            mediaPlayer.start()
            _mediaPlayerState.update { MediaPlayerState.Started }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Pause media player
     */
    private fun pauseMediaPlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _mediaPlayerState.update { MediaPlayerState.Paused }
        }
    }

    private fun changeAudioProgress(newValue: Float) {
        when (mediaPlayerState.value) {
            MediaPlayerState.Initializing -> Unit
            else -> {
                mediaPlayer.seekTo(newValue.toInt())
            }
        }
    }
}

/**
 * Media player state
 *
 * @constructor Create empty Media player state
 */
sealed class MediaPlayerState {
    object Initializing : MediaPlayerState()
    object Initialized : MediaPlayerState()
    object Started : MediaPlayerState()
    object Paused : MediaPlayerState()
    object Finished : MediaPlayerState()
}