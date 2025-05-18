package com.example.hbomax.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.hbomax.ui.moviedetail.PlayerViewModel
import com.example.hbomax.ui.moviedetail.PlayerViewModelFactory

@Composable
fun PlayerScreen(
    movieId: Int?, // MovieId passed from navigation
    navController: NavController // To navigate back
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val playerViewModel: PlayerViewModel = viewModel(
        factory = PlayerViewModelFactory(application, movieId)
    )

    val player by playerViewModel.player.collectAsState()

    // Handle player lifecycle with screen lifecycle
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(player, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player?.pause()
                Lifecycle.Event.ON_RESUME -> player?.play()
                Lifecycle.Event.ON_DESTROY -> player?.release() // Should be handled by ViewModel's onCleared
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // ViewModel's onCleared will handle final release
        }
    }


    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black), // Player screen often has black background
            contentAlignment = Alignment.Center
        ) {
            if (player != null) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player
                            useController = true // Show default Media3 controls
                        }
                    },
                    update = { view ->
                        // PlayerView updates are handled internally by setting player
                        // Or if you need to re-set it due to player instance change:
                        view.player = player
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f) // Common video aspect ratio
                )
            } else {
                CircularProgressIndicator() // Show loading while player is being prepared
            }
        }
    }
}
