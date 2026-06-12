package com.example.chambape.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chambape.di.AppModule
import com.example.chambape.presentation.auth.SkillsScreen
import com.example.chambape.presentation.home.CreateJobScreen
import com.example.chambape.presentation.home.MyJobsScreen
import com.example.chambape.presentation.messages.ChatScreen
import com.example.chambape.presentation.messages.MessagesScreen
import com.example.chambape.presentation.notifications.NotificationsScreen
import com.example.chambape.presentation.profile.EditProfileScreen
import com.example.chambape.presentation.profile.ProfileScreen
import com.example.chambape.presentation.profile.SettingsScreen
import com.example.chambape.presentation.profile.WalletScreen
import com.example.chambape.presentation.shifts.MyShiftsScreen
import com.example.chambape.presentation.shifts.ShiftSummaryScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(AppModule.authRepository, AppModule.tokenManager)
    )
    val userRole by mainViewModel.userRole.collectAsState()
    val isContratante = userRole == "CONTRATANTE"

    Scaffold(
        bottomBar = { ChambaPeBottomBar(navController, isContratante) }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Routes.HomeFeed.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            // Tab: Home — pasa mainNavController
            composable(Routes.HomeFeed.route) {
                val homeNav = rememberNavController()
                HomeNavHost(
                    navController     = homeNav,
                    mainNavController = navController  // ← pasa el main
                )
            }

            // Tab: Shifts (chambeador) / Jobs (contratante)
            composable(Routes.MyShifts.route) {
                if (isContratante) {
                    MyJobsScreen(
                        onNavigateToCreateJob = { navController.navigate(Routes.CreateJob.route) },
                        onNotificationsClick  = { navController.navigate(Routes.Notifications.route) }
                    )
                } else {
                    MyShiftsScreen(
                        onShiftClick         = { shiftId ->
                            navController.navigate(Routes.ShiftSummary.createRoute(shiftId))
                        },
                        onNotificationsClick = { navController.navigate(Routes.Notifications.route) }
                    )
                }
            }

            // Crear chamba (accesible desde la pestaña Jobs del contratante)
            composable(Routes.CreateJob.route) {
                CreateJobScreen(
                    onBack    = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() }
                )
            }
            composable(
                route     = Routes.ShiftSummary.route,
                arguments = listOf(navArgument("shiftId") { type = NavType.StringType })
            ) { back ->
                ShiftSummaryScreen(
                    shiftId = back.arguments?.getString("shiftId") ?: "",
                    onDone  = { navController.popBackStack() }
                )
            }

            // Tab: Messages
            composable(Routes.Messages.route) {
                MessagesScreen(
                    onChatClick = { conversationId, jobId ->
                        navController.navigate(Routes.Chat.createRoute(conversationId, jobId))
                    }
                )
            }
            composable(
                route     = Routes.Chat.route,
                arguments = listOf(
                    navArgument("conversationId") { type = NavType.StringType },
                    navArgument("jobId")          { type = NavType.StringType }
                )
            ) { back ->
                ChatScreen(
                    conversationId = back.arguments?.getString("conversationId") ?: "",
                    jobId          = back.arguments?.getString("jobId") ?: "",
                    onBack         = { navController.popBackStack() }
                )
            }

            // Tab: Profile
            composable(Routes.Profile.route) {
                ProfileScreen(
                    onGoToEditProfile   = { navController.navigate(Routes.EditProfile.route) },
                    onGoToMyShifts      = { navController.navigate(Routes.MyShiftsFromProfile.route) },
                    onGoToSettings      = { navController.navigate(Routes.Settings.route) },
                    onGoToWallet        = { navController.navigate(Routes.Wallet.route) },
                    onGoToSkills        = { navController.navigate(Routes.SkillsFromProfile.route) },
                    onGoToNotifications = { navController.navigate(Routes.Notifications.route) },
                    onLogout            = {
                        navController.navigate(Routes.Start.route) {
                            popUpTo(Routes.Main.route) { inclusive = true }
                        }
                    }
                )
            }

            // Notifications
            composable(Routes.Notifications.route) {
                NotificationsScreen(onBack = { navController.popBackStack() })
            }

            // Edit Profile
            composable(Routes.EditProfile.route) {
                EditProfileScreen(onBack = { navController.popBackStack() })
            }

            // Settings
            composable(Routes.Settings.route) {
                SettingsScreen(
                    onBack   = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate(Routes.Start.route) {
                            popUpTo(Routes.Main.route) { inclusive = true }
                        }
                    }
                )
            }

            // Wallet
            composable(Routes.Wallet.route) {
                WalletScreen(onBack = { navController.popBackStack() })
            }

            // MyShifts desde Profile (con back)
            composable(Routes.MyShiftsFromProfile.route) {
                MyShiftsScreen(
                    onShiftClick         = { shiftId ->
                        navController.navigate(Routes.ShiftSummary.createRoute(shiftId))
                    },
                    onBack               = { navController.popBackStack() },
                    onNotificationsClick = { navController.navigate(Routes.Notifications.route) }
                )
            }

            // Skills desde Profile (con back)
            composable(Routes.SkillsFromProfile.route) {
                SkillsScreen(
                    onContinue = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun ChambaPeBottomBar(navController: NavHostController, isContratante: Boolean) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest   = backStackEntry?.destination

    NavigationBar {
        MainTab.entries.forEach { tab ->
            // El contratante ve "Jobs" en lugar de "Shifts" en esa pestaña.
            val label = if (tab == MainTab.SHIFTS && isContratante) "Jobs" else tab.label
            NavigationBarItem(
                selected = currentDest?.hierarchy?.any { it.route == tab.route } == true,
                onClick  = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                icon  = { Icon(imageVector = tab.icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}