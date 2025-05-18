package com.example.hbomax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.hbomax.ui.AppNavHost
import com.example.hbomax.ui.theme.HBOMaxTheme
import com.example.hbomax.ui.theme.MaxBackgroundDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HBOMaxTheme {
                val navController = rememberNavController() // Create NavController
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaxBackgroundDark
                ) {
                    AppNavHost(navController = navController) // Set up navigation
                }
            }
        }
    }
}
