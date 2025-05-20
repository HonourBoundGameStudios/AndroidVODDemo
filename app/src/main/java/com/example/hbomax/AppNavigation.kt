package com.example.hbomax.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.hbomax.ui.moviedetail.PlayerScreen
import com.example.hbomax.MovieScreen // Your movie grid screen

object AppRoutes {
    const val HOME_ROUTE = "home"
    const val PLAYER_ROUTE = "player"
    const val SEARCH_ROUTE = "search"
    const val PROFILE_ROUTE = "profile"
    const val STYLES_GUIDE_ROUTE = "styles_guide"
}

object AppRoutesArguments {
    const val MOVIE_ID_ARG = "movieId"
}

// Define Bottom Navigation Items (can be in its own file or here)
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(AppRoutes.HOME_ROUTE, "Home", Icons.Filled.Home)
    object Search : BottomNavItem(AppRoutes.SEARCH_ROUTE, "Search", Icons.Filled.Search)
    object Profile : BottomNavItem(AppRoutes.PROFILE_ROUTE, "My Stuff", Icons.Filled.Person)
    object Settings : BottomNavItem(AppRoutes.STYLES_GUIDE_ROUTE, "Settings", Icons.Filled.Settings)
}

val bottomNavItemsList = listOf(
    BottomNavItem.Home,
    BottomNavItem.Search,
    BottomNavItem.Profile,
    BottomNavItem.Settings
)

@OptIn(ExperimentalMaterial3Api::class) // For Scaffold
@Composable
fun AppNavigationContainer(navController: NavHostController) { // Renamed to reflect it holds Scaffold
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determine if the bottom bar should be shown based on the current route
    // For example, don't show it on the PlayerScreen
    val showBottomBar = bottomNavItemsList.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) { // Only show BottomBar on specified screens
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    bottomNavItemsList.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(navController = navController, startDestination = AppRoutes.HOME_ROUTE, innerPadding = innerPadding)
    }
}


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    innerPadding: PaddingValues // Receive padding from Scaffold
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding) // Apply padding here
    ) {
        composable(route = AppRoutes.HOME_ROUTE) {
            MovieScreen(
                onMovieClick = { movieId ->
                    navController.navigate("${AppRoutes.PLAYER_ROUTE}/$movieId")
                }
            )
        }

        composable(
            route = "${AppRoutes.PLAYER_ROUTE}/{${AppRoutesArguments.MOVIE_ID_ARG}}",
            arguments = listOf(navArgument(AppRoutesArguments.MOVIE_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(AppRoutesArguments.MOVIE_ID_ARG)
            PlayerScreen(movieId = movieId, navController = navController)
        }

        composable(route = AppRoutes.SEARCH_ROUTE) {
            GenericScreenContent(name = "Search Screen", onNavigateBack = { navController.popBackStack() })
        }
        composable(route = AppRoutes.PROFILE_ROUTE) {
            GenericScreenContent(name = "Profile Screen", onNavigateBack = { navController.popBackStack() })
        }
        // Add StylesScreen route if needed
        composable(route = AppRoutes.STYLES_GUIDE_ROUTE) {
            StylesScreen(navController) { }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericScreenContent(name: String, onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(name) }, navigationIcon = { IconButton(onClick = onNavigateBack){ Icon(Icons.AutoMirrored.Filled.ArrowBack, "")}} ) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Content for $name")
        }
    }
}