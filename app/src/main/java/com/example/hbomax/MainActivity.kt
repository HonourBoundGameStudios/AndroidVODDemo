package com.example.hbomax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.hbomax.ui.AppNavigationContainer // Changed from AppNavHost
import com.example.hbomax.ui.theme.HBOMaxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HBOMaxTheme {
                val navController = rememberNavController()
                AppNavigationContainer(navController = navController)
            }
        }
    }
}