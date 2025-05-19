package com.example.hbomax.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hbomax.ui.components.GradientButton
import com.example.hbomax.ui.navigation.bottomNavItems
import com.example.hbomax.ui.theme.HBOMaxTheme
import com.example.hbomax.ui.theme.MaxButtonGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylesScreen(navController: NavHostController, onNavigateBack: () -> Unit) {

    var selectedItemIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Component Style Guide") },
                navigationIcon = {
                    // Assuming you might navigate *to* this screen, so a back button is useful
                    // If it's a root screen in a debug build, this might not be needed or could be different
                    // For now, let's assume we can navigate back
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    // If no back navigation from here, you can omit navigationIcon or use a placeholder/menu
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant, // Or primary
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant // Or onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface, // Or surfaceVariant
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // TODO: Handle navigation based on item.route

                            navController.navigate(item.route)

                        },
                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary, // Or your MaxVibrantPurple
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer // Or a subtle primary variant
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Make the content scrollable
                .padding(16.dp), // Add padding around the content
            verticalArrangement = Arrangement.spacedBy(24.dp) // Space between component groups
        ) {
            // Sections for different component types will go here
            TypographySection()
            Spacer(modifier = Modifier.height(16.dp)) // Extra space
            ButtonSection()
            Spacer(modifier = Modifier.height(16.dp))
            CardSection()
            // TextFieldSection()
            // IconSection()
            // Etc.
        }
    }
}

@Composable
fun CardSection() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionTitle("Cards")
        // Add your card components here
        MovieCard(
            title = "Minecraft",
            width = 234.dp,
            height = 132.dp,
            imageUrl = "",
            genre = "Action/Adventure/Sci-Fi",
            onClick = { /* Handle click */ }
        )
        Spacer(Modifier.height(16.dp))
        MovieCard(
            title = "Minecraft",
            width = 356.dp,
            height = 200.dp,
            imageUrl = "",
            genre = "Action/Adventure/Sci-Fi",
            onClick = { /* Handle click */ }
        )
        Spacer(Modifier.height(16.dp))
        MovieCard(
            title = "Minecraft",
            width = 173.dp,
            height = 173.dp,
            imageUrl = "",
            genre = "Action/Adventure/Sci-Fi",
            onClick = { /* Handle click */ }
        )
        Spacer(Modifier.height(16.dp))
        MovieCard(
            title = "Minecraft",
            width = 112.dp,
            height = 168.dp,
            imageUrl = "",
            genre = "Action/Adventure/Sci-Fi",
            onClick = { /* Handle click */ }
        )
        Spacer(Modifier.height(16.dp))
        MovieCard(
            title = "Minecraft",
            width = 173.dp,
            height = 260.dp,
            imageUrl = "",
            genre = "Action/Adventure/Sci-Fi",
            onClick = { /* Handle click */ }
        )
        Spacer(Modifier.height(16.dp))
        MovieCard(
            title = "Minecraft",
            width = 234.dp,
            height = 351.dp,
            imageUrl = "",
            genre = "Action/Adventure/Sci-Fi",
            onClick = { /* Handle click */ }
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall, // Or your custom headline style
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun TypographySection() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionTitle("Typography")
        Text("Display Large", style = MaterialTheme.typography.displayLarge)
        Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
        Text("Headline Medium", style = MaterialTheme.typography.headlineMedium)
        Text("Title Large", style = MaterialTheme.typography.titleLarge)
        Text("Title Medium", style = MaterialTheme.typography.titleMedium)
        Text("Body Large", style = MaterialTheme.typography.bodyLarge)
        Text("Body Medium", style = MaterialTheme.typography.bodyMedium)
        Text("Body Small", style = MaterialTheme.typography.bodySmall)
        Text("Button Text", style = MaterialTheme.typography.labelMedium)
        Text("Caption Text", style = MaterialTheme.typography.labelMedium)
        Text("Overline Text", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun ButtonSection() {
    Column() {
        SectionTitle("Buttons")

        GradientButton(
            text = "ELIGE UN PLAN",
            textStyle = MaterialTheme.typography.labelMedium,
            onClick = { /*TODO*/ }
        )

        GradientButton(
            text = "Normal Grey Button",
            onClick = { /*TODO*/ },
            gradientColors = listOf(MaxButtonGrey, MaxButtonGrey),
            textStyle = MaterialTheme.typography.labelMedium
        )
    }
}
