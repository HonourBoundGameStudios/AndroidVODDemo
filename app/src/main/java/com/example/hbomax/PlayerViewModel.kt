package com.example.hbomax.ui.moviedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.hbomax.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

// Define UI states for the PlayerScreen, including loading the video URL
sealed interface PlayerUiState {
    object Idle : PlayerUiState // Initial state
    object LoadingVideoUrl : PlayerUiState
    data class PlayerReady(val exoPlayer: ExoPlayer) : PlayerUiState
    data class Error(val message: String) : PlayerUiState
}

class PlayerViewModel(
    private val application: Application, // Keep application for ExoPlayer Builder
    private val movieId: Int?
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Idle)
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    // A known good, directly playable HLS stream as a fallback
    private val fallbackVideoUrl = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"
    private var exoPlayerInstance: ExoPlayer? = null

    init {
        Log.d("PlayerViewModel", "Initializing for movieId: $movieId")
        if (movieId != null) {
            fetchVideoAndPreparePlayer(movieId)
        } else {
            Log.e("PlayerViewModel", "MovieID is null, cannot fetch video.")
            // If no movieId, we could play a default video or show an error
            // For now, let's play the fallback if movieId is null for some reason
            preparePlayerWithUrl(fallbackVideoUrl, "Fallback due to null movieId")
        }
    }

    private fun fetchVideoAndPreparePlayer(id: Int) {
        _uiState.value = PlayerUiState.LoadingVideoUrl
        Log.d("PlayerViewModel", "Fetching video URL for movieId: $id")

        viewModelScope.launch {
            try {
                Log.d("PlayerViewModel", "Fetching videos for movieId: $id")
                val videoResponse = RetrofitClient.instance.getMovieVideos(movieId = id)
                Log.d("PlayerViewModel", "Video response: $videoResponse")

                // Find a suitable video, e.g., a Trailer from YouTube
                val anyYouTubeVideo = videoResponse.results.find { it.site == "YouTube" }

                var streamUrl: String? = null
                var sourceDescription = "No suitable video found"

                val youtubeVideoKey = anyYouTubeVideo?.videoKey

                if (youtubeVideoKey != null) {
                    // ExoPlayer CANNOT play YouTube URLs (like https://www.youtube.com/watch?v=KEY) directly
                    // without a specific YouTube extractor library (e.g., NewPipeExtractor or similar).
                    // This is complex to add quickly.
                    // So, for this demo, if we find a YouTube key, we'll LOG it but still use the fallback.
                    Log.i("PlayerViewModel", "Found YouTube video key: $youtubeVideoKey. Direct playback of YouTube URLs is not supported by default ExoPlayer.")
                    sourceDescription = "Found YouTube trailer (key: $youtubeVideoKey), using fallback stream for demo."
                    // In a real app with a YouTube extractor:
                    // streamUrl = getStreamableUrlFromYouTubeKey(youtubeVideoKey) // This function would use an extractor
                }

                // If no YouTube video or if we're intentionally using fallback:
                if (streamUrl == null) {
                    Log.w("PlayerViewModel", "$sourceDescription. Using fallback HLS stream.")
                    streamUrl = fallbackVideoUrl
                }

                preparePlayerWithUrl(streamUrl, sourceDescription)

            } catch (e: IOException) {
                Log.e("PlayerViewModel", "Network error fetching video URL: ${e.message}", e)
                _uiState.value = PlayerUiState.Error("Network error: Could not load video information.")
                // Optionally, still try to play fallback on network error for videos endpoint
                preparePlayerWithUrl(fallbackVideoUrl, "Fallback due to network error fetching video list")
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error fetching/processing video URL: ${e.message}", e)
                _uiState.value = PlayerUiState.Error("Error: Could not load video information.")
                preparePlayerWithUrl(fallbackVideoUrl, "Fallback due to general error fetching video list")
            }
        }
    }

    private fun preparePlayerWithUrl(videoUrl: String, sourceDescription: String) {
        viewModelScope.launch { // Ensure player init is on a coroutine scope if not already
            try {
                Log.d("PlayerViewModel", "Preparing player with URL: $videoUrl (Source: $sourceDescription)")
                // Release existing player if any
                exoPlayerInstance?.release()

                val newPlayer = ExoPlayer.Builder(application).build().apply {
                    setMediaItem(MediaItem.fromUri(videoUrl))
                    prepare()
                    playWhenReady = true // Autoplay
                }
                exoPlayerInstance = newPlayer
                _uiState.value = PlayerUiState.PlayerReady(newPlayer)

                newPlayer.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        // Log state or update UI
                        val state = when(playbackState) {
                            Player.STATE_IDLE -> "IDLE"
                            Player.STATE_BUFFERING -> "BUFFERING"
                            Player.STATE_READY -> "READY"
                            Player.STATE_ENDED -> "ENDED"
                            else -> "UNKNOWN"
                        }
                        Log.d("PlayerViewModel", "Player state: $state")
                    }
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        Log.e("PlayerViewModel", "Player error: ${error.message}", error)
                        _uiState.value = PlayerUiState.Error("Playback error: ${error.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error initializing ExoPlayer instance: ${e.message}", e)
                _uiState.value = PlayerUiState.Error("Failed to initialize player.")
            }
        }
    }

    fun retry() {
        if (movieId != null) {
            fetchVideoAndPreparePlayer(movieId)
        } else {
            _uiState.value = PlayerUiState.Error("Cannot retry: Movie ID is missing.")
        }
    }

    override fun onCleared() {
        Log.d("PlayerViewModel", "onCleared called, releasing player.")
        exoPlayerInstance?.release()
        exoPlayerInstance = null
        super.onCleared()
    }
}

// Factory remains the same
class PlayerViewModelFactory(
    private val application: Application,
    private val movieId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(application, movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}