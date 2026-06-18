package com.example.chambape.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chambape.di.AppModule
import com.example.chambape.presentation.auth.LoginScreen
import com.example.chambape.presentation.auth.PhoneVerificationScreen
import com.example.chambape.presentation.auth.ProfileSetupScreen
import com.example.chambape.presentation.auth.RegisterScreen
import com.example.chambape.presentation.auth.SkillsScreen
import com.example.chambape.presentation.auth.StartScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // Si ya hay sesión activa, arranca directo en Main; si no, en Start.
    val startDestination = if (AppModule.tokenManager.isLoggedIn())
        Routes.Main.route
    else
        Routes.Login.route

    NavHost(
        navController    = navController,
        startDestination = startDestination
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
                        popUpTo(0) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Routes.Register.route) }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.PhoneVerification.route) },
                onGoToLogin       = { navController.navigate(Routes.Login.route) }
            )
        }

        composable(Routes.PhoneVerification.route) {
            PhoneVerificationScreen(
                onVerified = { navController.navigate(Routes.ProfileSetup.route) }
            )
        }

        composable(Routes.ProfileSetup.route) {
            ProfileSetupScreen(
                onNext = { navController.navigate(Routes.Skills.route) }
            )
        }

        composable(Routes.Skills.route) {
            SkillsScreen(
                onContinue = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Main.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}