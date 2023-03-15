package com.skoove.challenge.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RatingRepository {
    val ratingMap: StateFlow<Map<String, Int>>
    fun getRating(audioUri: String): Flow<Int>
    fun setRating(audioUri: String, rating: Int)
}