package com.example.hbomax.ui.moviedetail

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect // Import LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

// Assume PlayerUiState includes:
// sealed interface PlayerUiState {
//     // ... other states
//     data class YouTubeKeyFound(val youtubeKey: String) : PlayerUiState
// }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    movieId: Int?,
    navController: NavController
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val playerViewModel: PlayerViewModel = viewModel(
        factory = PlayerViewModelFactory(application, movieId)
    )
    val uiState by playerViewModel.uiState.collectAsState()

    // Lifecycle management for the player (if using ExoPlayer directly)
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, uiState) {
        val observer = LifecycleEventObserver { _, event ->
            val currentPlayer = (uiState as? PlayerUiState.PlayerReady)?.exoPlayer
            when (event) {
                Lifecycle.Event.ON_PAUSE -> currentPlayer?.pause()
                Lifecycle.Event.ON_RESUME -> currentPlayer?.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Handle automatic navigation to YouTube when key is found
    if (uiState is PlayerUiState.YouTubeKeyFound) {
        val youtubeKeyState = uiState as PlayerUiState.YouTubeKeyFound // Smart cast
        LaunchedEffect(key1 = youtubeKeyState.youtubeKey) { // Re-launch if key changes (unlikely here)
            val youtubeAppIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${youtubeKeyState.youtubeKey}"))
            val youtubeWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${youtubeKeyState.youtubeKey}"))

            try {
                context.startActivity(youtubeAppIntent)
            } catch (e: ActivityNotFoundException) {
                // YouTube app not found, try web browser
                context.startActivity(youtubeWebIntent)
            }
            // After attempting to launch, navigate back from the player screen
            // as its purpose (launching YouTube) is done.
            // You might want a slight delay or a confirmation, but for direct launch, popBackStack is common.
            navController.popBackStack()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie ID: ${movieId ?: "N/A"}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is PlayerUiState.Idle, PlayerUiState.LoadingVideoUrl -> {
                    CircularProgressIndicator()
                    Text(
                        if (state is PlayerUiState.LoadingVideoUrl) "Loading video..." else "Initializing...",
                        color = Color.White,
                        modifier = Modifier.padding(top = 60.dp)
                    )
                }
                is PlayerUiState.PlayerReady -> {
                    AndroidView(
                        factory = { ctx -> PlayerView(ctx).apply { player = state.exoPlayer; useController = true } },
                        update = { view -> view.player = state.exoPlayer },
                        modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f)
                    )
                }
                is PlayerUiState.YouTubeKeyFound -> {
                    // Content for when YouTube key is found but before LaunchedEffect triggers
                    // or if it fails to pop back stack immediately.
                    CircularProgressIndicator()
                    Text(
                        "Opening trailer in YouTube...",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is PlayerUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { playerViewModel.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}