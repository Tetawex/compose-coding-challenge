package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.entity.ListAudioEntry
import com.skoove.challenge.domain.repository.AudioEntryRepository
import com.skoove.challenge.domain.repository.FavoriteRepository
import com.skoove.challenge.domain.repository.RatingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAudioDetailUseCase(
    private val audioEntryRepository: AudioEntryRepository,
    private val favoriteRepository: FavoriteRepository,
    private val ratingRepository: RatingRepository
) {
    fun getAudioDetail(audioUri: String): Flow<ListAudioEntry?> {
        return combine(
            audioEntryRepository.getAudioByUri(audioUri),
            favoriteRepository.favoriteAudioUri,
            ratingRepository.getRating(audioUri)
        ) { audioEntry, favoriteUri, rating ->
            if (audioEntry == null) {
                null
            } else {
                ListAudioEntry(
                    entry = audioEntry,
                    isFavorite = audioEntry.audio == favoriteUri,
                    rating = rating
                )
            }
        }
    }
}