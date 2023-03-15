package com.skoove.challenge.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
 * Favorite component. Set / unset favorite
 */
@Composable
fun FavoriteElement(modifier: Modifier,
                    favoriteState: Boolean,
                    onClick: (newState: Boolean) -> Unit) {

    Icon(painter = painterResource(id = if (favoriteState) R.drawable.ic_heart_filled else R.drawable.ic_heart),
         contentDescription = stringResource(id = R.string.contentDescription_audio_is_favorite),
         modifier = modifier
             .testTag("FavoriteElement")
             .size(32.dp)
             .clickable(
                     interactionSource = remember { MutableInteractionSource() },
                     indication = rememberRipple(bounded = false),
             ) {
                 onClick(!favoriteState)
             }
    )
}