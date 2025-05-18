package com.example.hbomax.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp), // For very subtle rounding
    small = RoundedCornerShape(4.dp),      // Smaller components, some buttons
    medium = RoundedCornerShape(8.dp),     // Default for cards, dialogs, most buttons
    large = RoundedCornerShape(12.dp),     // Larger cards or prominent rounded elements
    extraLarge = RoundedCornerShape(16.dp) // For elements needing significant rounding
)