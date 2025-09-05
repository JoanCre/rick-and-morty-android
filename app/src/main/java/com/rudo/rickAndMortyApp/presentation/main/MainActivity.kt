package com.rudo.rickAndMortyApp.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.rudo.rickAndMortyApp.presentation.navigation.AppNavHost
import com.rudo.rickAndMortyApp.presentation.ui.theme.MiAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application.
 * This activity is annotated with `@AndroidEntryPoint` to enable dependency injection with Hilt.
 *
 * 1. Create a custom theme.
 * 2. Set up your NavHost.
 * 3. Use the theme and the NavHost inside `setContent`.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            MiAppTheme {
                AppNavHost(navController = navHostController)
            }
        }
    }
}
