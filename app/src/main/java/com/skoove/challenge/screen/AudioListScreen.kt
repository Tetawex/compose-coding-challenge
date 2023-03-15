package com.skoove.challenge.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import com.skoove.challenge.viewmodel.AudioListViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skoove.challenge.component.AudioListItem
import com.skoove.challenge.domain.entity.AudioEntry
import com.skoove.challenge.viewmodel.AudioListState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AudioListScreen(
    audioListViewModel: AudioListViewModel = koinViewModel(),
    navigateToAudioDetail: (audioUri: String) -> Unit
) {
    val state by audioListViewModel.state.collectAsState(AudioListState.Default)
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, "Error loading data", Toast.LENGTH_SHORT).show()
        }
    }

    val pullRefreshScope = rememberCoroutineScope()
    fun refresh() = pullRefreshScope.launch { audioListViewModel.loadData() }
    val pullRefreshState =
        rememberPullRefreshState(state.isRefreshing, ::refresh)

    Column(Modifier.fillMaxSize()) {
        TopAppBar(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Skoovin'",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .clipToBounds()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                items(items = state.list) { listAudio ->
                    AudioListItem(
                        isFavorite = listAudio.isFavorite,
                        rating = listAudio.rating,
                        audioEntry = listAudio.entry,
                        onItemClicked = { navigateToAudioDetail(listAudio.entry.audio) },
                        onFavoriteClicked = {
                            audioListViewModel.onFavoriteClicked(
                                listAudio.entry.audio,
                                it
                            )
                        }
                    )
                }
            }

            if (state.list.isNotEmpty()) {
                PullRefreshIndicator(
                    state.isRefreshing,
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isModalLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Button(onClick = audioListViewModel::loadData) {
                            Text(text = "RETRY LOADING")
                        }
                    }
                }
            }
        }
    }
}