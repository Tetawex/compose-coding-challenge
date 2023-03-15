package com.skoove.challenge.data.repository

import com.skoove.challenge.domain.repository.RatingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex

class InMemoryRatingRepository : RatingRepository {
    private val _ratings = MutableStateFlow(mutableMapOf<String, Int>())

    override val ratingMap
        get() = _ratings

    override fun getRating(audioUri: String): Flow<Int> = ratingMap.map {
        it.getOrElse(audioUri) { 0 }
    }

    override fun setRating(audioUri: String, rating: Int) {
        val mapCopy = _ratings.value.toMutableMap()
        mapCopy[audioUri] = rating
        _ratings.value = mapCopy
    }
}