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
import com.example.hbomax.ui.StylesScreen
import com.example.hbomax.ui.theme.HBOMaxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HBOMaxTheme {
                StylesScreen(onNavigateBack = {}) // Dummy onNavigateBack for now

//                val navController = rememberNavController() // Create NavController
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    AppNavHost(navController = navController) // Set up navigation
//                }
            }
        }
    }
}
