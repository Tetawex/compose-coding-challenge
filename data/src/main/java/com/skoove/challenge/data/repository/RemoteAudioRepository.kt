package com.skoove.challenge.data.repository

import com.skoove.challenge.data.api.SkooveApiClient
import com.skoove.challenge.domain.entity.AudioEntry
import com.skoove.challenge.domain.repository.AudioEntryRepository
import kotlinx.coroutines.flow.*
import retrofit2.await

class RemoteAudioRepository(val apiClient: SkooveApiClient) : AudioEntryRepository {
    private val audioEntryMap = mutableMapOf<String, MutableStateFlow<AudioEntry?>>()
    private val _audioEntries = MutableStateFlow(emptyList<AudioEntry>())

    override val audioEntries: StateFlow<List<AudioEntry>> = _audioEntries.asStateFlow()

    override suspend fun loadAudioEntries() {
        val response = apiClient.getAudioData().await()

        response?.data?.map {
            val entry = AudioEntry(
                title = it.title,
                audio = it.audio,
                cover = it.cover,
                totalDurationMs = it.totalDurationMs
            )

            val existingInstance = audioEntryMap.getOrDefault(entry.audio, null)
            if (existingInstance != null) {
                existingInstance.value = entry
            } else {
                audioEntryMap[it.audio] = MutableStateFlow(entry)
            }

            return@map entry
        }?.let { list ->
            _audioEntries.value = list
        }
    }

    override fun getAudioByUri(audioUri: String): StateFlow<AudioEntry?> {
        return audioEntryMap.getOrElse(audioUri) { MutableStateFlow(null) }
    }
}