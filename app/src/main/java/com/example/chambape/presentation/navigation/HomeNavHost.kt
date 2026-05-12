package com.example.chambape.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chambape.presentation.home.ActiveShiftScreen
import com.example.chambape.presentation.home.ApplyScreen
import com.example.chambape.presentation.home.HomeFeedScreen
import com.example.chambape.presentation.home.JobDetailsScreen

@Composable
fun HomeNavHost(navController: NavHostController) {
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
                    navController.popBackStack(Routes.HomeFeed.route, inclusive = false)
                }
            )
        }

        composable(
            route = Routes.ActiveShift.route,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { back ->
            val jobId = back.arguments?.getString("jobId") ?: ""

            ActiveShiftScreen(
                onClose = {
                    navController.popBackStack(Routes.HomeFeed.route, inclusive = false)
                },
                onConfirmArrival = {
                    navController.navigate(Routes.Apply.createRoute(jobId))
                }
            )
        }
    }
}
