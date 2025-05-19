package com.example.hbomax.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppFontFamily = NunitoSans

// Define TextStyles based on the Figma specifications
val hboMaxHeadline = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp
    // lineHeight and letterSpacing can be added if specified or desired
)

val hboMaxSubtitle1 = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Normal, // "Regular"
    fontSize = 16.sp
)

val hboMaxSubtitle2 = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Normal, // "Regular"
    fontSize = 14.sp
)

val hboMaxBody2 = TextStyle( // Assuming this is the primary body text
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Normal, // "REGULAR"
    fontSize = 14.sp
)

val hboMaxButton = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    letterSpacing = 0.10.sp
)

val hboMaxCaption = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Normal, // "Regular"
    fontSize = 12.sp
)

// Now, map these to the Material 3 Typography scale
// It's a good idea to fill out most Material roles, even if mapping multiple
// spec styles to the same Material role or one spec style to multiple Material roles.
val Typography = Typography(
    // Display Styles (Typically larger than headlines)
    // We don't have explicit "Display" from the spec, so we can map Headline here or make them larger.
    displayLarge = hboMaxHeadline.copy(
        fontSize = 48.sp,
        fontWeight = FontWeight.SemiBold
    ), // Example: making it larger
    displayMedium = hboMaxHeadline.copy(fontSize = 36.sp, fontWeight = FontWeight.SemiBold),
    displaySmall = hboMaxHeadline.copy(
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal
    ), // Example regular display

    // Headline Styles
    headlineLarge = hboMaxHeadline, // Direct map from "Headline - Semibold - 24px"
    headlineMedium = hboMaxHeadline.copy(fontSize = 22.sp), // Slightly smaller headline
    headlineSmall = hboMaxSubtitle1.copy(fontWeight = FontWeight.SemiBold), // Using Subtitle1 size but semibold

    // Title Styles (For AppBars, Dialog Titles etc.)
    titleLarge = hboMaxSubtitle1.copy(fontWeight = FontWeight.Medium), // Good for prominent titles
    titleMedium = hboMaxSubtitle1, // "Subtitle 1 - Regular - 16px"
    titleSmall = hboMaxSubtitle2.copy(fontWeight = FontWeight.Medium),

    // Body Styles
    bodyLarge = hboMaxSubtitle1, // Can use Subtitle1 as a larger body text
    bodyMedium = hboMaxBody2,    // "BODY 2 - REGULAR - 14PX"
    bodySmall = hboMaxCaption,   // "Caption - Regular - 12px" can serve as smaller body text

    // Label Styles (For Buttons, smaller text elements)
    labelLarge = hboMaxButton,
    labelMedium = hboMaxButton.copy(fontSize = 14.sp),
    labelSmall = hboMaxButton.copy(fontSize = 12.sp)
)
