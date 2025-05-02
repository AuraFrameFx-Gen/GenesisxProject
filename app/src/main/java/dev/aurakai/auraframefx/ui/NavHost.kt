package dev.aurakai.auraframefx.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AuraNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(
                onNavigateToChat = { navController.navigate("chat") },
                onNavigateToEcosystem = { navController.navigate("ecosystem") }
            )
        }
        composable("chat") {
            AIChatScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("ecosystem") {
            EcosystemMenuScreen()
        }
    }
}
