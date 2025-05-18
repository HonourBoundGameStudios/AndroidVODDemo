package com.example.hbomax

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hbomax.ui.theme.MaxOnPrimaryDark
import com.example.hbomax.ui.theme.MaxOnSecondaryDark
import com.example.hbomax.ui.theme.MaxWhite


@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(MaxOnPrimaryDark, MaxOnSecondaryDark) // Example
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .background(Brush.horizontalGradient(colors = gradientColors), shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 16.dp, vertical = 8.dp), // Intrinsic padding for button feel
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Make container transparent to show gradient
            contentColor = MaxWhite
        ),
        shape = MaterialTheme.shapes.medium // Apply shape to the button itself for ripple
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}