package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.repository.RatingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SetAudioRatingUseCaseTest {
    @Test
    fun `Updates ratings repository`() {
        val mockRepository = mockk<RatingRepository>()
        every { mockRepository.setRating(TEST_AUDIO_URL_1, 3) } returns Unit

        SetAudioRatingUseCase(mockRepository)(TEST_AUDIO_URL_1, 3)

        verify { mockRepository.setRating(TEST_AUDIO_URL_1, 3) }
    }
}