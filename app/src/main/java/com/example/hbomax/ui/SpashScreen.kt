package com.example.hbomax.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hbomax.R
import com.example.hbomax.ui.SplashUiState
import com.example.hbomax.ui.SplashViewModel

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = viewModel(),
    onSplashFinished: () -> Unit // Callback to navigate to the main app
) {
    val uiState by splashViewModel.uiState.collectAsState()
    var imageLoadedSuccessfully by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fallback background color
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No visual ripple
                onClick = onSplashFinished // Navigate on click
            ),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is SplashUiState.Loading -> {
                Image(
                    painter = painterResource(id = R.drawable.hbo_max_white_logo_image),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(250.dp)
                )
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            is SplashUiState.Success -> {
                if (state.backdropUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.backdropUrl)
                            .crossfade(true)
                            .listener(
                                onSuccess = { _, _ -> imageLoadedSuccessfully = true },
                                onError = { _, _ -> imageLoadedSuccessfully = false }
                            )
                            .build(),
                        contentDescription = "Splash Screen Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.hbo_max_white_logo_image),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(250.dp)
                )
                // ADDED "Tap to continue" text for user guidance
                Text(
                    text = "Tap to continue",
                    color = Color.White.copy(alpha = 0.7f), // Use a color from your theme if available
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }
            is SplashUiState.Error -> {
                Image(
                    painter = painterResource(id = R.drawable.hbo_max_white_logo_image),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(250.dp)
                )
                // Displaying "Tap to continue" even on error, so user can proceed
                Text(
                    text = "Tap to continue", // Or state.message + "\nTap to continue"
                    color = Color.White.copy(alpha = 0.7f), // Use a theme color
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }
        }
    }
}
