package com.example.hbomax.ui.moviedetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Simple ViewModel that just prepares a player with a hardcoded URL
class PlayerViewModel(application: Application, private val movieId: Int?) : ViewModel() {
    private val _player = MutableStateFlow<ExoPlayer?>(null)
    val player: StateFlow<ExoPlayer?> = _player

    // TODO: Replace with actual video URL logic based on movieId if available
    // For now, a sample public HLS stream
    private val sampleVideoUrl = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"

    init {
        // You could use movieId here to fetch movie-specific video URLs if your API supported it
        println("PlayerViewModel created for movieId: $movieId")

        viewModelScope.launch {
            val exoPlayer = ExoPlayer.Builder(application).build().apply {
                setMediaItem(MediaItem.fromUri(sampleVideoUrl))
                prepare()
                playWhenReady = true // Autoplay
            }
            _player.value = exoPlayer

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    // Handle playback state changes (e.g., for telemetry or UI updates)
                }
            })
        }
    }

    override fun onCleared() {
        _player.value?.release()
        _player.value = null
        super.onCleared()
    }
}

// Factory to pass movieId and application context to PlayerViewModel
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