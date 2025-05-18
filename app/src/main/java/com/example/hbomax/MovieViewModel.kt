package com.example.hbomax // Adjust package name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

// Define UI states
sealed interface MovieUiState {
    object Loading : MovieUiState
    data class Success(val movies: List<Movie>) : MovieUiState
    data class Error(val message: String) : MovieUiState
}

class MovieViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        _uiState.value = MovieUiState.Loading // Set loading state
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getPopularMovies()
                _uiState.value = MovieUiState.Success(response.results)
            } catch (e: IOException) { // Network error
                _uiState.value = MovieUiState.Error("Network error: ${e.localizedMessage}")
            } catch (e: Exception) { // Other errors (e.g., parsing)
                _uiState.value = MovieUiState.Error("Failed to fetch movies: ${e.localizedMessage}")
            }
        }
    }
}