package com.skoove.challenge.domain.entity

data class ListAudioEntry(
    val entry: AudioEntry,
    val isFavorite: Boolean,
    val rating: Int
)