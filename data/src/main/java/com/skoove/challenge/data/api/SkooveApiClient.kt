package com.skoove.challenge.data.api

import com.skoove.challenge.data.response.ApiResponse
import com.skoove.challenge.domain.entity.AudioEntry
import retrofit2.Call
import retrofit2.http.GET

interface SkooveApiClient {
    @GET("manifest.json")
    fun getAudioData(): Call<ApiResponse<List<AudioEntry>>?>
}