package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.entity.ListAudioEntry
import com.skoove.challenge.domain.repository.AudioEntryRepository
import com.skoove.challenge.domain.repository.FavoriteRepository
import com.skoove.challenge.domain.repository.RatingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.withContext

class GetAudioListUseCase(
    private val audioEntryRepository: AudioEntryRepository,
    favoriteRepository: FavoriteRepository,
    ratingRepository: RatingRepository,
) {
    val audioEntryList: Flow<List<ListAudioEntry>> =
        combine(
            audioEntryRepository.audioEntries,
            favoriteRepository.favoriteAudioUri,
            ratingRepository.ratingMap,
        ) { entries, favoriteUri, ratingMap ->
            entries.map {
                ListAudioEntry(
                    entry = it,
                    isFavorite = it.audio == favoriteUri,
                    rating = ratingMap[it.audio] ?: 0
                )
            }
        }.conflate()

    suspend fun loadAudioEntries() = withContext(Dispatchers.IO) {
        audioEntryRepository.loadAudioEntries()
    }
}