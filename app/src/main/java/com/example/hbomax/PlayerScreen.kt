package com.example.hbomax.ui.moviedetail

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController

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
    var activePlayer: androidx.media3.common.Player? = null // Keep track of the active player instance

    // Lifecycle management for the player
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, uiState) { // Re-run if uiState changes to PlayerReady
        val observer = LifecycleEventObserver { _, event ->
            val currentPlayer = (uiState as? PlayerUiState.PlayerReady)?.exoPlayer
            when (event) {
                Lifecycle.Event.ON_PAUSE -> currentPlayer?.pause()
                Lifecycle.Event.ON_RESUME -> currentPlayer?.play()
                // ViewModel's onCleared handles final release
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie ID: ${movieId ?: "N/A"}") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Before popping back, ensure player is released by ViewModel if screen is destroyed
                        // Though onCleared in ViewModel should handle this.
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
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
                is PlayerUiState.Idle -> {
                    // This state might be very brief
                    CircularProgressIndicator()
                    Text("Initializing...", color = Color.White, modifier = Modifier.padding(top = 60.dp))
                }
                is PlayerUiState.LoadingVideoUrl -> {
                    CircularProgressIndicator()
                    Text("Loading video...", color = Color.White, modifier = Modifier.padding(top = 60.dp))
                }
                is PlayerUiState.PlayerReady -> {
                    activePlayer = state.exoPlayer // Update active player
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = state.exoPlayer
                                useController = true
                            }
                        },
                        // update is called when the PlayerReady state (and thus exoPlayer instance) changes
                        update = { view ->
                            view.player = state.exoPlayer
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
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