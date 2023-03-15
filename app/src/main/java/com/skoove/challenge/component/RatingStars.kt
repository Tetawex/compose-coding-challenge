package com.skoove.challenge.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skoove.challenge.R

/**
 * Rating stars Component to show audio rating with filled stars
 */
@Composable
fun RatingStars(modifier: Modifier,
                rating: Int,
                ratingMax: Int = 5,
                starSize: Int = 24,
                onStarClicked: (index: Int) -> Unit) {

    Row(modifier = modifier.testTag("RatingStars")) {
        repeat(ratingMax) { index ->
            Icon(painter = painterResource(id = if (index >= rating) R.drawable.ic_star else R.drawable.ic_star_filled),
                 contentDescription = stringResource(id = R.string.contentDescription_audio_rating_start),
                 modifier = Modifier
                     .size(starSize.dp)
                     .testTag("star")
                     .padding(horizontal = 1.dp)
                     .clickable(interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false)) {
                         onStarClicked(index)
                     }
            )
        }
    }
}