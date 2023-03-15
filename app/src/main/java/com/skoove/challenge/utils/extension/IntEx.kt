package com.skoove.challenge.utils.extension

import kotlin.math.floor

/**
 * Convert Int number to minutes and seconds
 */
fun Int.timeStampToDuration(): String {
    val totalSeconds = floor(this / 1000f).toInt()
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds - (minutes * 60)
    return if (this < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)
}