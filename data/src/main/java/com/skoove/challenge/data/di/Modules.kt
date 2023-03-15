package com.skoove.challenge.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skoove.challenge.data.BuildConfig
import com.skoove.challenge.data.api.SkooveApiClient
import com.skoove.challenge.data.repository.InMemoryFavoriteRepository
import com.skoove.challenge.data.repository.InMemoryRatingRepository
import com.skoove.challenge.data.repository.RemoteAudioRepository
import com.skoove.challenge.domain.repository.AudioEntryRepository
import com.skoove.challenge.domain.repository.FavoriteRepository
import com.skoove.challenge.domain.repository.RatingRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

object Modules {
    val common = module {
        single {
            createJson()
        }
        single {
            createHttpClient()
        }
        single {
            createRetrofit(
                json = get(),
                okHttpClient = get()
            )
        }
        single {
            createSkooveApiClient(retrofit = get())
        }
        single<AudioEntryRepository> {
            RemoteAudioRepository(apiClient = get())
        }
        single<FavoriteRepository> {
            InMemoryFavoriteRepository()
        }
        single<RatingRepository> {
            InMemoryRatingRepository()
        }
    }


    @OptIn(ExperimentalSerializationApi::class)
    fun createJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun createRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.CONNECTION_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Suppress("KotlinConstantConditions")
    fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.BUILD_TYPE != "release") {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    })
                }
            }.build()

    fun createSkooveApiClient(retrofit: Retrofit): SkooveApiClient {
        return retrofit.create(SkooveApiClient::class.java)
    }
}

