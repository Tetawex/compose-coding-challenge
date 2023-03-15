package com.skoove.challenge.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Composable
fun MyApp() {
    MaterialTheme {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()

        Scaffold(scaffoldState = scaffoldState) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding))
            {
                MyNavGraph(navController = navController)
            }

        }
    }
}
