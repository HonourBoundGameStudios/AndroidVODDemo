package com.example.hbomax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.hbomax.ui.theme.HBOMaxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HBOMaxTheme {
                    MovieScreen() // Call your main screen composable
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HBOMaxTheme {
        MovieScreen() // Call your main screen composable
    }
}