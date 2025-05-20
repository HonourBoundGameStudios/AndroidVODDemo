package com.example.hbomax.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hbomax.MovieUiState
import com.example.hbomax.PopularMovieViewModel
import com.example.hbomax.services.TMDBApiServiceProvider
import com.example.hbomax.ui.theme.HBOMaxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    popularMovieViewModel: PopularMovieViewModel = PopularMovieViewModel(),
    onMovieClick: (Int) -> Unit
) {
    val uiState by popularMovieViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Popular Movies") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is MovieUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MovieUiState.Success -> {
                    if (state.movies.isEmpty()) {
                        Text(
                            text = "No movies found.",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        MovieListGrid(movies = state.movies, onMovieClick = onMovieClick)
                    }
                }

                is MovieUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                MovieUiState.Loading -> TODO()
            }
        }
    }
}

@Composable
fun MovieListGrid(
    movies: List<Movie>, onMovieClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 3 columns
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // Spacing between rows
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between columns
    ) {
        items(movies, key = { movie -> movie.id }) { movie -> // Add key for better performance
            MovieGridItem(
                movie = movie, onMovieClick = { onMovieClick(movie.id) }) // Handle click)
        }
    }
}

@Composable
fun MovieGridItem(
    movie: Movie, onMovieClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(2f / 3f)
            .clickable { onMovieClick(movie.id) } // Handle click
            .fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Box(contentAlignment = Alignment.BottomCenter) { // For overlaying text later if needed
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(TMDBApiServiceProvider.imageBaseUrl + movie.posterPath)
                    .crossfade(true) // Optional: for smooth transition
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// --- Previews (Optional but helpful) ---
@Preview(showBackground = true)
@Composable
fun MovieGridItemPreview() {
    HBOMaxTheme {
        MovieGridItem(
            movie = Movie(
                id = 1,
                title = "Sample Movie Title That Is Quite Long",
                posterPath = "/qhb1qOilapbapxWQn9jtRCMFRXU.jpg",
                overview = "This is a sample overview.",
                releaseDate = "2023-10-01"
            ), onMovieClick = {})
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MovieScreenLoadingPreview() {
    HBOMaxTheme { // Replace with your theme
        // To preview loading, you'd ideally mock the ViewModel or pass a specific state.
        // For simplicity, directly show a progress indicator.
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MovieScreenSuccessPreview() {
    HBOMaxTheme { // Replace with your theme
        MovieScreen(
            onMovieClick = {}
        )
    }
}