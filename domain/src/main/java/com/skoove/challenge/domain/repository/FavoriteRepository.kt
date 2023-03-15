package com.skoove.challenge.domain.repository

import kotlinx.coroutines.flow.MutableStateFlow

interface FavoriteRepository {
    val favoriteAudioUri: MutableStateFlow<String?>
}