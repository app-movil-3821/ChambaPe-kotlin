package com.example.chambape.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chambape.presentation.auth.LoginScreen
import com.example.chambape.presentation.auth.RegisterScreen
import com.example.chambape.presentation.auth.SkillsScreen
import com.example.chambape.presentation.auth.StartScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = Routes.Login.route
    ) {

        composable(Routes.Start.route) {
            StartScreen(
                onGetStarted = { navController.navigate(Routes.Register.route) },
                onLogin      = { navController.navigate(Routes.Login.route) }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess  = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.Start.route) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Routes.Register.route) }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.Skills.route) },
                onGoToLogin       = { navController.navigate(Routes.Login.route) }
            )
        }

        composable(Routes.Skills.route) {
            SkillsScreen(
                onContinue = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.Start.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Main.route) {
            MainScreen()
        }
    }
}
