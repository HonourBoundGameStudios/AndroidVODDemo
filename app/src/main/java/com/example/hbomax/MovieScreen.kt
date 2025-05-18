package com.example.hbomax

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
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
import com.example.hbomax.ui.theme.HBOMaxTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(movieViewModel: MovieViewModel = MovieViewModel()) {
    val uiState by movieViewModel.uiState.collectAsState()

    Scaffold( // Provides basic Material Design layout structure
        topBar = {
            TopAppBar(
                title = { Text("Popular Movies") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues -> // Content padding provided by Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
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
                        MovieListGrid(movies = state.movies)
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
            }
        }
    }
}

@Composable
fun MovieListGrid(movies: List<Movie>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columns
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // Spacing between rows
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between columns
    ) {
        items(movies, key = { movie -> movie.id }) { movie -> // Add key for better performance
            MovieGridItem(movie = movie)
        }
    }
}

@Composable
fun MovieGridItem(movie: Movie) {
    Card(
        modifier = Modifier
            .aspectRatio(1f) // 1:1 Aspect Ratio for the cell
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(contentAlignment = Alignment.BottomCenter) { // For overlaying text later if needed
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(RetrofitClient.imageBaseUrl + movie.posterPath)
                    .crossfade(true) // Optional: for smooth transition
                    .error(android.R.drawable.ic_menu_gallery) // Placeholder for error
                    .placeholder(android.R.drawable.ic_menu_rotate) // Placeholder while loading
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop, // Crop to fill 1:1 cell
                modifier = Modifier.fillMaxSize()
            )
            // Optional: Add movie title overlay
            // Text(
            //     text = movie.title,
            //     modifier = Modifier
            //         .fillMaxWidth()
            //         .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
            //         .padding(4.dp),
            //     color = MaterialTheme.colorScheme.onPrimaryContainer,
            //     textAlign = TextAlign.Center,
            //     maxLines = 2,
            //     overflow = TextOverflow.Ellipsis,
            //     style = MaterialTheme.typography.labelSmall
            // )
        }
    }
}

// --- Previews (Optional but helpful) ---
@Preview(showBackground = true)
@Composable
fun MovieGridItemPreview() {
    HBOMaxTheme { // Replace with your theme
        MovieGridItem(
            movie = Movie(
                id = 1,
                title = "Sample Movie Title That Is Quite Long",
                posterPath = "/qhb1qOilapbapxWQn9jtRCMFRXU.jpg", // Example path
                overview = "This is a sample overview."
            )
        )
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
    val sampleMovies = listOf(
        Movie(1, "Movie 1", "/path1.jpg", "Overview 1"),
        Movie(2, "Movie 2", "/path2.jpg", "Overview 2"),
        Movie(3, "Movie 3", "/path3.jpg", "Overview 3"),
        Movie(4, "Movie 4", "/path4.jpg", "Overview 4"),
        Movie(5, "Movie 5", "/path5.jpg", "Overview 5")
    )
    HBOMaxTheme { // Replace with your theme
        Scaffold(topBar = { TopAppBar(title = { Text("Popular Movies") }) }) { padding ->
            Box(Modifier.padding(padding)) {
                MovieListGrid(movies = sampleMovies)
            }
        }
    }
}