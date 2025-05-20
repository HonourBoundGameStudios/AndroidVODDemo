package com.example.hbomax.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.hbomax.R

@Composable
fun MovieCard(
    title: String,
    imageUrl: String,
    genre: String,
    onClick: () -> Unit,
    width: Dp,
    height: Dp
) {
    // Implement your MovieCard here
    // This is a placeholder implementation
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.Companion
            .height(height)
            .width(width),
    ) {
        Box(
        ) {
            if (imageUrl == "") {
                Image(
                    painter = painterResource(id = R.drawable.minecraft_poster_16x9_2k),
                    contentDescription = "Placeholder",
                    contentScale = ContentScale.Companion.Crop,
                    modifier = Modifier.Companion
                        .height(height)
                        .width(width)
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Movie Poster",
                    modifier = Modifier.Companion
                        .height(height)
                        .width(width)
                )
            }

            Column(
                Modifier.Companion.align(Alignment.Companion.BottomStart).fillMaxWidth()
                    .background(
                        Brush.Companion.verticalGradient(
                            colors = listOf(
                                Color.Companion.Transparent,
                                Color.Companion.Black.copy(alpha = 0.7f), // Start of more opaque section
                                Color.Companion.Black.copy(alpha = 0.9f)  // Bottom of scrim, more opaque
                            )
                        )
                    )
            )
            {
                Spacer(modifier = Modifier.Companion.height(16.dp))
                Text(
                    text = title, style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.Companion.padding(4.dp)
                )
                Text(
                    text = genre, style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.Companion.padding(4.dp)
                )
            }
        }
    }
}