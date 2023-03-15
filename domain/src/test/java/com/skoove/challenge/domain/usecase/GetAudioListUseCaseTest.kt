package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.entity.AudioEntry
import com.skoove.challenge.domain.entity.ListAudioEntry
import com.skoove.challenge.domain.repository.AudioEntryRepository
import com.skoove.challenge.domain.repository.FavoriteRepository
import com.skoove.challenge.domain.repository.RatingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAudioListUseCaseTest {
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `Maps repository data`() = runTest {
        val audioEntryRepository = mockk<AudioEntryRepository>()
        val listFlow = MutableStateFlow(
            listOf(
                AudioEntry(
                    title = "Song 1",
                    audio = TEST_AUDIO_URL_1,
                    cover = "https://test/test.jpg",
                    totalDurationMs = 10000
                ),
                AudioEntry(
                    title = "Song 2",
                    audio = TEST_AUDIO_URL_2,
                    cover = "https://test/test.jpg",
                    totalDurationMs = 5000
                )
            )
        )
        every { audioEntryRepository.audioEntries } returns listFlow

        val favoriteRepository = mockk<FavoriteRepository>()
        val favoriteFlow = MutableStateFlow<String?>(TEST_AUDIO_URL_1)
        every { favoriteRepository.favoriteAudioUri } returns favoriteFlow

        val ratingRepository = mockk<RatingRepository>()
        val ratingFlow = MutableStateFlow(
            mapOf(
                TEST_AUDIO_URL_1 to 5
            )
        )
        every { ratingRepository.ratingMap } returns ratingFlow


        val getAudioListUseCase =
            GetAudioListUseCase(audioEntryRepository, favoriteRepository, ratingRepository)

        val entries = getAudioListUseCase.audioEntryList.first()
        assertEquals(
            entries[0].entry, AudioEntry(
                title = "Song 1",
                audio = TEST_AUDIO_URL_1,
                cover = "https://test/test.jpg",
                totalDurationMs = 10000
            )
        )
        assertEquals(entries[0].isFavorite, true)
        assertEquals(entries[0].rating, 5)

        assertEquals(
            entries[1].entry, AudioEntry(
                title = "Song 2",
                audio = TEST_AUDIO_URL_2,
                cover = "https://test/test.jpg",
                totalDurationMs = 5000
            )
        )
        assertEquals(entries[1].isFavorite, false)
        assertEquals(entries[1].rating, 0)
    }
}