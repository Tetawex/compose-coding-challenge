package com.skoove.challenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skoove.challenge.domain.entity.ListAudioEntry
import com.skoove.challenge.domain.usecase.GetAudioListUseCase
import com.skoove.challenge.domain.usecase.SetFavoriteAudioUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class AudioListState(
    val list: List<ListAudioEntry>,
    val isModalLoading: Boolean,
    val isRefreshing: Boolean,
    val error: Throwable? = null
){
    companion object {
        val Default = AudioListState(
            list = emptyList(),
            isModalLoading = false,
            isRefreshing = false
        )
    }
}

class AudioListViewModel(
    private val getAudioListUseCase: GetAudioListUseCase,
    private val setFavoriteAudioUseCase: SetFavoriteAudioUseCase
) : ViewModel() {
    private val loadingState = MutableStateFlow(false)
    private val errorState = MutableStateFlow<Throwable?>(null)

    val state: Flow<AudioListState> =
        combine(
            getAudioListUseCase.audioEntryList,
            loadingState,
            errorState
        ) { list, isLoadingData, error ->
            AudioListState(
                list = list,
                isModalLoading = isLoadingData && list.isEmpty(),
                isRefreshing = isLoadingData && list.isNotEmpty(),
                error = error
            )
        }

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                loadingState.value = true
                errorState.value = null

                getAudioListUseCase.loadAudioEntries()
            } catch (e: Throwable) {
                errorState.value = e
            } finally {
                loadingState.value = false
            }
        }
    }


    fun onFavoriteClicked(audioUri: String, isFavorite: Boolean) {
        setFavoriteAudioUseCase(audioUri, isFavorite)
    }
}
