package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.update

class SetFavoriteAudioUseCase(private val favoriteRepository: FavoriteRepository) {
    operator fun invoke(audioUri: String, isFavorite: Boolean) {
        favoriteRepository.favoriteAudioUri.update { prevFavoriteUri ->
            if (isFavorite) {
                audioUri
            } else {
                if (prevFavoriteUri == audioUri) {
                    null
                } else {
                    prevFavoriteUri
                }
            }
        }
    }
}