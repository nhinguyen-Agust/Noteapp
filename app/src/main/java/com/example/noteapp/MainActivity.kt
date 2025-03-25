package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(
                        onLogin = { userId ->
                            navController.navigate("notes/$userId") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
                composable("notes/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId") ?: ""
                    NoteApp(
                        userId = userId,
                        onSignOut = {
                            navController.navigate("login") {
                                popUpTo("notes/$userId") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}