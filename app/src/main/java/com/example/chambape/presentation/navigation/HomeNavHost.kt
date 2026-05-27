package com.example.chambape.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chambape.presentation.home.ActiveShiftScreen
import com.example.chambape.presentation.home.ApplyScreen
import com.example.chambape.presentation.home.HelpScreen
import com.example.chambape.presentation.home.HomeFeedScreen
import com.example.chambape.presentation.home.JobDetailsScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    mainNavController: NavHostController  // ← recibe el main
) {
    NavHost(
        navController    = navController,
        startDestination = Routes.HomeFeed.route
    ) {
        composable(Routes.HomeFeed.route) {
            HomeFeedScreen(
                onJobClick = { jobId ->
                    navController.navigate(Routes.JobDetails.createRoute(jobId))
                }
            )
        }

        composable(
            route     = Routes.JobDetails.route,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { back ->
            JobDetailsScreen(
                jobId   = back.arguments?.getString("jobId") ?: "",
                onBack  = { navController.popBackStack() },
                onApply = { jobId ->
                    navController.navigate(Routes.Apply.createRoute(jobId))
                }
            )
        }

        composable(
            route     = Routes.Apply.route,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { back ->
            ApplyScreen(
                jobId       = back.arguments?.getString("jobId") ?: "",
                onConfirmed = {
                    navController.navigate(
                        Routes.ActiveShift.createRoute(
                            back.arguments?.getString("jobId") ?: ""
                        )
                    ) {
                        popUpTo(Routes.HomeFeed.route)
                    }
                }
            )
        }

        composable(
            route     = Routes.ActiveShift.route,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) {
            ActiveShiftScreen(
                onClose          = {
                    navController.popBackStack(Routes.HomeFeed.route, inclusive = false)
                },
                onConfirmArrival = {
                    mainNavController.navigate(Routes.ShiftSummary.createRoute("1")) {
                        popUpTo(Routes.HomeFeed.route)
                    }
                },
                onHelp = { navController.navigate(Routes.Help.route) }
            )
        }

        composable(Routes.Help.route) {
            HelpScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}