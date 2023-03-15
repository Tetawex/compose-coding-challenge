package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class SetFavoriteAudioUseCaseTest {
    @Test
    fun `Updates favorite repository`() {
        val mockRepository = object : FavoriteRepository {
            override val favoriteAudioUri: MutableStateFlow<String?> = MutableStateFlow(null)
        }
        val setFavoriteAudio = SetFavoriteAudioUseCase(mockRepository)

        setFavoriteAudio(TEST_AUDIO_URL_1, true)
        assertEquals(mockRepository.favoriteAudioUri.value, TEST_AUDIO_URL_1)

        setFavoriteAudio(TEST_AUDIO_URL_2, false)
        assertEquals(mockRepository.favoriteAudioUri.value, TEST_AUDIO_URL_1)

        setFavoriteAudio(TEST_AUDIO_URL_2, true)
        assertEquals(mockRepository.favoriteAudioUri.value, TEST_AUDIO_URL_2)

        setFavoriteAudio(TEST_AUDIO_URL_2, false)
        assertEquals(mockRepository.favoriteAudioUri.value, null)
    }
}