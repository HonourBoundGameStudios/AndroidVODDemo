package com.example.hbomax.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hbomax.ui.search.SearchUiState
import com.example.hbomax.ui.search.SearchViewModel
import com.example.hbomax.ui.theme.HBOMaxTheme

// Import MovieUiState from your popular movies if it's the same, or define one for search

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    // Assuming this screen is part of your NavHost, it might receive NavController
    // For simplicity, onMovieClick will be handled directly here.
    onMovieClick: (Int) -> Unit,
    searchViewModel: SearchViewModel = viewModel()
) {
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val uiState by searchViewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Padding for the whole screen
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchViewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Search Movies") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        searchViewModel.onSearchQueryChanged("") // Clear query
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear Search")
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                // Optionally trigger search immediately on keyboard search action
                // searchViewModel.performSearch(searchQuery) // Debounce already handles it
                keyboardController?.hide()
                focusManager.clearFocus()
            })
        )

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            when (val state = uiState) {
                is SearchUiState.Idle -> {
                    Text("Start typing to search for movies.", textAlign = TextAlign.Center)
                }
                is SearchUiState.EmptyQuery -> {
                    Text("Search cleared. Type to find movies.", textAlign = TextAlign.Center)
                }
                is SearchUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is SearchUiState.Success -> {
                    if (state.movies.isEmpty()) {
                        Text("No movies found for your query.", textAlign = TextAlign.Center)
                    } else {
                        // Reuse MovieListGrid
                        MovieListGrid(movies = state.movies, onMovieClick = onMovieClick)
                    }
                }
                is SearchUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview_Idle() {
    HBOMaxTheme {
        // For preview, you might need to provide a mock ViewModel or a specific state
        // Here, we're relying on the default state of a fresh ViewModel
        SearchScreen(onMovieClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview_Results() {
    // This preview would require mocking the ViewModel to provide SearchUiState.Success
    HBOMaxTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(value = "Matrix", onValueChange = {}, label = {Text("Search")}, modifier = Modifier.fillMaxWidth())
            Text("Search results would appear here.")
        }
    }
}