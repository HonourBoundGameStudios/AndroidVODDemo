package com.example.hbomax.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hbomax.MovieScreen

// Define route constants
object AppDestinations {
    const val MOVIE_GRID_ROUTE = "movie_grid"
    const val PLAYER_ROUTE = "player"
    const val MOVIE_ID_ARG = "movieId" // Argument name for passing movie ID
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.MOVIE_GRID_ROUTE
    ) {
        // Movie Grid Screen
        composable(route = AppDestinations.MOVIE_GRID_ROUTE) {
            MovieScreen(
                onMovieClick = { movieId ->
                    // Navigate to player screen, passing the movie ID
                    navController.navigate("${AppDestinations.PLAYER_ROUTE}/$movieId")
                }
            )
        }

        // Player Screen
        composable(
            route = "${AppDestinations.PLAYER_ROUTE}/{${AppDestinations.MOVIE_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.MOVIE_ID_ARG) {
                type = NavType.IntType // Or StringType if your ID is a string
            })
        ) { backStackEntry ->
            // Retrieve the movieId from the arguments
            val movieId = backStackEntry.arguments?.getInt(AppDestinations.MOVIE_ID_ARG)
            // Or getString if you used StringType
            PlayerScreen(
                movieId = movieId,
                navController = navController
            )
        }
    }
}