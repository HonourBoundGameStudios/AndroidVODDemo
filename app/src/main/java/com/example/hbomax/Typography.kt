package com.example.hbomax

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val MaxFontFamily = FontFamily.SansSerif // Example: Max uses a font like "Circular Std" or a custom one

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Bold, // Figma "Display Bold"
        fontSize = 48.sp, // Estimate
        lineHeight = 56.sp
    ),
    displayMedium = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp, // Estimate
        lineHeight = 44.sp
    ),
    displaySmall = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Normal, // Figma "Display Regular"
        fontSize = 32.sp, // Estimate
        lineHeight = 40.sp
    ),

    // Headline styles (can map to larger "Text Bold" from Figma or smaller "Display")
    headlineLarge = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Bold, // Figma "Text Bold" for prominent text
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Normal, // Figma "Text Regular" for prominent text
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),

    // Title styles (for app bars, dialog titles)
    titleLarge = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Bold, // Often bold
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    // Body styles (from Figma "Text")
    bodyLarge = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Normal, // Figma "Text Regular"
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    // Label styles (for buttons, captions)
    labelLarge = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Medium, // Buttons often medium or bold
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = MaxFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)