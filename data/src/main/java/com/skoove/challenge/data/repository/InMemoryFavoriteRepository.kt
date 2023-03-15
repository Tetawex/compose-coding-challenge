package com.skoove.challenge.data.repository

import com.skoove.challenge.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow

class InMemoryFavoriteRepository : FavoriteRepository {
    override val favoriteAudioUri = MutableStateFlow<String?>(null)
}