package com.example.hbomax.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hbomax.ui.AppDestinations

sealed class BottomNavItem(
        val route: String, // A route string if this integrates with full NavController
        val title: String,
        val icon: ImageVector
) {
    // Temporary routes for the bottom navigation items
    object Home : BottomNavItem(AppDestinations.MOVIE_GRID_ROUTE, "Home", Icons.Filled.Home)
    object Search : BottomNavItem(AppDestinations.STYLES_GUIDE_ROUTE, "Search", Icons.Filled.Search)
    object Profile : BottomNavItem(AppDestinations.STYLES_GUIDE_ROUTE, "My Stuff", Icons.Filled.Person)
    object Settings : BottomNavItem(AppDestinations.STYLES_GUIDE_ROUTE, "Settings", Icons.Filled.Settings)
}