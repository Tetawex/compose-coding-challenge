package com.skoove.challenge.domain.entity

import kotlinx.serialization.Serializable

/**
 * Audio object
 */
@Serializable
data class AudioEntry(
    val title: String,
    val audio: String,
    val cover: String,
    val totalDurationMs: Int,
): java.io.Serializable