package com.skoove.challenge.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skoove.challenge.R
import com.skoove.challenge.domain.entity.AudioEntry
import com.skoove.challenge.ui.MediaPlayerController
import com.skoove.challenge.ui.MediaPlayerState
import com.skoove.challenge.utils.extension.timeStampToDuration
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage

/**
 * Audio item for the detail view
 */
@Composable
fun AudioDetailItem(
    audio: AudioEntry,
    mediaPlayer: MediaPlayerController,
    isFavorite: Boolean,
    duration: Int,
    rating: Int,
    onStarClicked: (rating: Int) -> Unit,
    onFavoriteClicked: (favorite: Boolean) -> Unit
) {
    val sliderProgress by mediaPlayer.progressSliderValue.collectAsState()
    val mediaPlayerState by mediaPlayer.mediaPlayerState.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 24.dp
            ),
            text = audio.title, fontSize = 24.sp
        )
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    mediaPlayer.audioSelected(audio.audio)
                },
            contentAlignment = Alignment.Center
        ) {
            // Audio Cover
            CoilImage(
                imageModel = audio.cover,
                contentDescription = null,
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = MaterialTheme.colors.surface
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
            )

            when (mediaPlayerState) {
                MediaPlayerState.Initializing -> {
                    if (mediaPlayerState is MediaPlayerState.Initializing) {
                        Box(
                            Modifier
                                .matchParentSize()
                                .background(Color(0x6DFFFFFF))
                        ) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }

                    }
                }
                else -> {
                    // Media Player Controller Icons
                    Image(
                        painter = painterResource(
                            id = if (mediaPlayerState is MediaPlayerState.Started) {
                                R.drawable.ic_pause
                            } else {
                                R.drawable.ic_play
                            }
                        ),
                        contentDescription = stringResource(id = R.string.contentDescription_audio_is_favorite),
                        modifier = Modifier
                            .size(120.dp)
                    )
                }
            }

            // audio favorite status element
            FavoriteElement(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
                favoriteState = isFavorite,
                onClick = {
                    onFavoriteClicked(!isFavorite)
                })
        }


        Spacer(modifier = Modifier.size(32.dp))

        // Time
        Text(
            modifier = Modifier.wrapContentWidth(),
            textAlign = TextAlign.Center,
            text = "${
                sliderProgress.toInt().timeStampToDuration()
            } / ${duration.timeStampToDuration()}",
            color = MaterialTheme.colors.onSurface
        )

        // Audio Slider
        Slider(
            value = sliderProgress,
            onValueChange = {
                mediaPlayer.dragProgressSlider(it)
            },
            valueRange = 0f..duration.toFloat(),
            onValueChangeFinished = {
                mediaPlayer.releaseProgressSlider()
            },
            steps = 1000,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondary,
                activeTickColor = MaterialTheme.colors.secondary,
                inactiveTickColor = MaterialTheme.colors.onError,
            )
        )

        Spacer(modifier = Modifier.size(32.dp))

        // Rating
        RatingStars(modifier = Modifier.padding(8.dp),
            rating = rating,
            starSize = 64,
            onStarClicked = { index ->
                onStarClicked(index + 1)
            })
    }

}
