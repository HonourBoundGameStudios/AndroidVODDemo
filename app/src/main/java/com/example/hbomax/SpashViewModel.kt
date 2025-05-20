package com.example.hbomax.ui // New package

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hbomax.services.TMDBApiServiceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


sealed interface SplashUiState {
    object Loading : SplashUiState
    data class Success(val backdropUrl: String?) : SplashUiState // Nullable if no backdrop
    data class Error(val message: String) : SplashUiState
}

class SplashViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        fetchRandomPopularMovieBackdrop()
    }

    fun fetchRandomPopularMovieBackdrop() {
        _uiState.value = SplashUiState.Loading
        viewModelScope.launch {
            try {
                val response = TMDBApiServiceProvider.instance.getPopularMovies()
                val randomMovie = response.results.firstOrNull()

                if (randomMovie?.posterPath != null) {
                    val fullPosterUrl = TMDBApiServiceProvider.imageBaseUrl + randomMovie.posterPath + randomMovie.posterPath
                    Log.d("SplashViewModel", "Splash fallback to poster URL: $fullPosterUrl")
                    _uiState.value = SplashUiState.Success(fullPosterUrl)
                }
                else {
                    Log.w("SplashViewModel", "No suitable movie or backdrop found for splash.")
                    _uiState.value = SplashUiState.Success(null) // Success, but no image
                }
            } catch (e: IOException) {
                Log.e("SplashViewModel", "Network error fetching splash image: ${e.message}", e)
                _uiState.value = SplashUiState.Error("Network error. Cannot load splash image.")
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error fetching splash image: ${e.message}", e)
                _uiState.value = SplashUiState.Error("Could not load splash image.")
            }
        }
    }
}