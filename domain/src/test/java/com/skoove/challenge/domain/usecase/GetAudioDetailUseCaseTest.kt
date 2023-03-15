package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.entity.AudioEntry
import com.skoove.challenge.domain.entity.ListAudioEntry
import com.skoove.challenge.domain.repository.AudioEntryRepository
import com.skoove.challenge.domain.repository.FavoriteRepository
import com.skoove.challenge.domain.repository.RatingRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAudioDetailUseCaseTest {
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `Maps repository data`() = runTest {
        val audioEntryRepository = mockk<AudioEntryRepository>()
        val testAudioEntry = AudioEntry(
            title = "Song 1",
            audio = TEST_AUDIO_URL_1,
            cover = "https://test/test.jpg",
            totalDurationMs = 10000
        )
        val audioEntryFlow = MutableStateFlow(testAudioEntry)
        val emptyFlow = MutableStateFlow<AudioEntry?>(null)
        every { audioEntryRepository.getAudioByUri(TEST_AUDIO_URL_1) } returns audioEntryFlow
        every { audioEntryRepository.getAudioByUri(TEST_AUDIO_URL_2) } returns emptyFlow

        val favoriteRepository = mockk<FavoriteRepository>()
        val favoriteFlow = MutableStateFlow<String?>(TEST_AUDIO_URL_1)
        every { favoriteRepository.favoriteAudioUri } returns favoriteFlow

        val ratingRepository = mockk<RatingRepository>()
        val ratingFlow = MutableStateFlow(5)
        val ratingFlow2 = MutableStateFlow(0)
        every { ratingRepository.getRating(TEST_AUDIO_URL_1) } returns ratingFlow
        every { ratingRepository.getRating(TEST_AUDIO_URL_2) } returns ratingFlow2


        val getAudioDetailUseCase =
            GetAudioDetailUseCase(audioEntryRepository, favoriteRepository, ratingRepository)

        assertEquals(
            getAudioDetailUseCase.getAudioDetail(TEST_AUDIO_URL_1).first(),
            ListAudioEntry(
                entry = testAudioEntry,
                rating = 5,
                isFavorite = true
            )
        )
        assertEquals(
            getAudioDetailUseCase.getAudioDetail(TEST_AUDIO_URL_2).first(), null
        )
    }
}