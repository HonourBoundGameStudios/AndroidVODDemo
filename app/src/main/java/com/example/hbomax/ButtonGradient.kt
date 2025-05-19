package com.example.hbomax.ui.components // Or wherever you keep custom components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hbomax.ui.theme.HBOMaxTheme
import com.example.hbomax.ui.theme.MaxButtonGradientEnd // Import your colors
import com.example.hbomax.ui.theme.MaxButtonGradientStart
import com.example.hbomax.ui.theme.MaxWhiteText

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(MaxButtonGradientStart, MaxButtonGradientEnd),
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    textStyle: TextStyle
) {
    Button(
        onClick = onClick,
        modifier = modifier, // Apply external modifiers first
        enabled = enabled,
        shape = MaterialTheme.shapes.small, // Define the shape for clipping and ripple
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Crucial: Button's own container is transparent
            contentColor = MaterialTheme.colorScheme.onPrimary,       // Color for the text content
            disabledContainerColor = Color.Transparent, // Handle disabled state transparency
            disabledContentColor = MaxWhiteText.copy(alpha = 0.38f) // Dim text when disabled
        )
    ) {
        // This RowScope is provided by the Button's content lambda
        // Apply the gradient background and padding to the content *inside* the button
        Row(
            Modifier
                .clip(MaterialTheme.shapes.extraSmall) // Clip the gradient to the button's shape
                .background(
                    brush = Brush.horizontalGradient(colors = if (enabled) gradientColors else gradientColors.map { it.copy(alpha = 0.5f) } ), // Dim gradient when disabled
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(contentPadding), // Apply the desired content padding here
            verticalAlignment = Alignment.CenterVertically, // From RowScope
            horizontalArrangement = Arrangement.Center // From RowScope
        ) {
            Text(
                text = text.uppercase(),
                style = textStyle
            )
        }
    }
}


// --- Preview ---
@Preview(showBackground = false) // Set background to false to see transparency effect if any
@Composable
fun GradientButtonPreview() {
    HBOMaxTheme { // Apply your theme for colors, shapes, typography
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GradientButton(
                text = "Enabled Gradient Button",
                onClick = {},
                textStyle = MaterialTheme.typography.labelMedium
            )
            GradientButton(
                text = "Disabled Gradient",
                onClick = {},
                enabled = false,
                textStyle = MaterialTheme.typography.labelMedium
            )

            // Example with different shape from theme
            GradientButton(
                text = "Small Shape Gradient",
                onClick = {},
                textStyle = MaterialTheme.typography.labelMedium // shape is applied to Button, background uses it
            )
        }
    }
}