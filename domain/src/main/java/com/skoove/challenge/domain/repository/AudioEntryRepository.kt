package com.skoove.challenge.domain.repository

import com.skoove.challenge.domain.entity.AudioEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AudioEntryRepository {
    val audioEntries: StateFlow<List<AudioEntry>>

    suspend fun loadAudioEntries()
    fun getAudioByUri(audioUri: String): StateFlow<AudioEntry?>
}