package com.example.hbomax.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type

private val MaxDarkColorScheme = darkColorScheme(
    primary = MaxPurpleDark,               // Main brand color, often for app bars, prominent buttons
    onPrimary = MaxOnPrimaryDark,          // Text/icons on primary color
    primaryContainer = MaxPurpleDarker,    // A slightly different shade of primary, for larger surfaces
    onPrimaryContainer = MaxOnPrimaryDark, // Text/icons on primaryContainer

    secondary = MaxPinkAccent,             // Accent color for floating action buttons, highlights
    onSecondary = MaxOnSecondaryDark,      // Text/icons on secondary color
    secondaryContainer = Color(0xFF8F0056), // A darker/muted version of pink for larger secondary areas
    onSecondaryContainer = MaxOnPrimaryDark,

    tertiary = MaxPinkAccent,              // Can be same as secondary or another accent
    onTertiary = MaxOnSecondaryDark,
    tertiaryContainer = Color(0xFF8F0056),
    onTertiaryContainer = MaxOnPrimaryDark,

    error = Color(0xFFFFB4AB),             // Standard error color
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = MaxBackgroundDark,        // Screen background
    onBackground = MaxOnBackgroundDark,    // Text/icons on background

    surface = MaxSurfaceDark,              // Card backgrounds, dialogs, menus
    onSurface = MaxOnSurfaceDark,          // Text/icons on surface
    surfaceVariant = Color(0xFF2c2b2f),    // Subtle variations of surface color
    onSurfaceVariant = MaxOnSurfaceDark,   // Text/icons on surfaceVariant

    outline = MaxOutlineDark,              // Borders, dividers
    inverseOnSurface = MaxOnBackgroundDark, // For elements that need to contrast with surface (rarely directly used)
    inverseSurface = MaxOnSurfaceDark,       // For elements that need to contrast with onSurface (rarely directly used)
    inversePrimary = MaxPurpleDark,        // For text/icons that need to be on an "inverted" primary (e.g. primary on light bg)
    surfaceTint = MaxPurpleDark,           // Tint color for surfaces, often same as primary
    outlineVariant = Color(0xFF49454F),   // A subtle variant of outline
    scrim = Color.Black                    // Color for overlay scrims (e.g., behind dialogs)
)

@Composable
fun HBOMaxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Default to system theme, Max is typically dark
    // Dynamic color is available on Android 12+
    // Max has strong branding, so dynamic color might be undesirable. Set to false.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // If dynamic color is enabled, prioritize it.
            // However, for strong branding like Max, you usually want to stick to brand colors.
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Always use MaxDarkColorScheme for this app, as Max is predominantly dark.
        // You can change this if you want to respect the system's light/dark mode strictly.
        // Forcing dark theme for this example:
        else -> MaxDarkColorScheme
        // Or, to respect system theme:
        // else -> if (darkTheme) MaxDarkColorScheme else MaxLightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            val insets = WindowCompat.getInsetsController(window, view)
            insets.isAppearanceLightStatusBars = false // For dark status bar icons on light SB
            insets.hide(Type.systemBars()) // Hide system bars for immersive experience
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Ensure you have a Typography.kt defined
        shapes = Shapes,         // Make sure you have a Shapes.kt defined
        content = content
    )
}