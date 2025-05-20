package com.example.hbomax.ui.search // New package

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hbomax.services.TMDBApiServiceProvider
import com.example.hbomax.ui.Movie

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface SearchUiState {
    object Idle : SearchUiState // Nothing searched yet, or empty query
    object Loading : SearchUiState
    data class Success(val movies: List<Movie>) : SearchUiState
    data class Error(val message: String) : SearchUiState
    object EmptyQuery : SearchUiState // Explicit state for when query is cleared
}

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private val debouncePeriodMillis = 500L // 500ms debounce

    init {
        // Observe searchQuery changes
        _searchQuery
            .debounce(debouncePeriodMillis) // Wait for 500ms of silence
            .filter { query -> // Only proceed if query is not blank (or has a minimum length)
                query.isNotBlank() && query.length > 2 // Example: min 3 characters
            }
            .distinctUntilChanged() // Only if the query text has actually changed
            .onEach { query -> // For each valid, debounced query
                performSearch(query)
            }
            .launchIn(viewModelScope) // Launch the collection in viewModelScope

        // Handle case where query becomes blank after a search
        _searchQuery
            .filter { it.isBlank() }
            .onEach {
                searchJob?.cancel() // Cancel any ongoing search
                _uiState.value = SearchUiState.EmptyQuery // Or Idle, depending on desired UX
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(query: String) {
        Log.d("SearchViewModel", "Performing search for: $query")
        _uiState.value = SearchUiState.Loading
        searchJob?.cancel() // Cancel previous job if any
        searchJob = viewModelScope.launch {
            try {
                val response = TMDBApiServiceProvider.instance.searchMovies(searchQuery = query)
                if (response.results.isNotEmpty()) {
                    _uiState.value = SearchUiState.Success(response.results)
                } else {
                    _uiState.value = SearchUiState.Error("No movies found for '$query'.")
                }
            } catch (e: IOException) {
                Log.e("SearchViewModel", "Network error during search: ${e.message}", e)
                _uiState.value = SearchUiState.Error("Network error. Please try again.")
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error during search: ${e.message}", e)
                _uiState.value = SearchUiState.Error("An error occurred. Please try again.")
            }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}