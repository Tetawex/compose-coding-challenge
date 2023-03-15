package com.skoove.challenge.domain.usecase

import com.skoove.challenge.domain.repository.RatingRepository

class SetAudioRatingUseCase(private val ratingRepository: RatingRepository) {
    operator fun invoke(audioUri: String, rating: Int) {
        ratingRepository.setRating(audioUri, rating);
    }
}