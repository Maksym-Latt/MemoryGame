package com.manacode.memorygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manacode.memorygame.ui.game.GameScreen
import com.manacode.memorygame.ui.history.HistoryScreen
import com.manacode.memorygame.ui.theme.MemoryGameTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoryGameTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "game"
    ) {
        composable("game") {
            GameScreen(
                onNavigateToHistory = {
                    navController.navigate("history") {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo("game") { inclusive = false }
                    }
                }
            )
        }
        composable("history") {
            HistoryScreen(
                onBack = {
                    navController.popBackStack("game", inclusive = false)
                }
            )
        }
    }
}
