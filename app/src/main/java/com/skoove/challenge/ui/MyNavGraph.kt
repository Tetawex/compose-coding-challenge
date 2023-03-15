package com.skoove.challenge.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.skoove.challenge.domain.entity.AudioEntry
import com.skoove.challenge.screen.AudioDetailScreen
import com.skoove.challenge.screen.AudioListScreen
import kotlinx.coroutines.InternalCoroutinesApi
import java.net.URLEncoder

object Destinations {
    const val ALERT_LIST_ROUTE = "alert_list"
    const val AUDIO_DETAIL_ROUTE = "audio_detail"

    object Arguments {
        const val AUDIO = "audio"
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@InternalCoroutinesApi
@Composable
fun MyNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.ALERT_LIST_ROUTE
) {

    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Destinations.ALERT_LIST_ROUTE) {
            AudioListScreen(navigateToAudioDetail = actions.navigateToAudioDetail)
        }
        composable(
            "${Destinations.AUDIO_DETAIL_ROUTE}/{${Destinations.Arguments.AUDIO}}",
            arguments = listOf(
                navArgument(Destinations.Arguments.AUDIO) {
                    nullable = true
                    type = NavType.StringType
                })
        ) {
            AudioDetailScreen(it.arguments?.getString(Destinations.Arguments.AUDIO)!!)
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val navigateToAudioDetail: (audioUri: String) -> Unit = { audioUri ->
        navController.currentBackStackEntry?.savedStateHandle?.apply {
            set(Destinations.Arguments.AUDIO, audioUri)
        }
        navController.navigate(
            "${Destinations.AUDIO_DETAIL_ROUTE}/${
                Uri.encode(
                    audioUri
                )
            }"
        )
    }
}

