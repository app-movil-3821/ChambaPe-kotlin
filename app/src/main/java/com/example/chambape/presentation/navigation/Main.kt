package com.example.chambape.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chambape.presentation.auth.SkillsScreen
import com.example.chambape.presentation.messages.ChatScreen
import com.example.chambape.presentation.messages.MessagesScreen
import com.example.chambape.presentation.profile.EditProfileScreen
import com.example.chambape.presentation.profile.ProfileScreen
import com.example.chambape.presentation.profile.SettingsScreen
import com.example.chambape.presentation.profile.WalletScreen
import com.example.chambape.presentation.shifts.MyShiftsScreen
import com.example.chambape.presentation.shifts.ShiftSummaryScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { ChambaPeBottomBar(navController) }
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

            // Tab: Shifts
            composable(Routes.MyShifts.route) {
                MyShiftsScreen(
                    onShiftClick = { shiftId ->
                        navController.navigate(Routes.ShiftSummary.createRoute(shiftId))
                    }
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
                    onChatClick = { chatId ->
                        navController.navigate(Routes.Chat.createRoute(chatId))
                    }
                )
            }
            composable(
                route     = Routes.Chat.route,
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { back ->
                ChatScreen(
                    chatId = back.arguments?.getString("chatId") ?: "",
                    onBack = { navController.popBackStack() }
                )
            }

            // Tab: Profile
            composable(Routes.Profile.route) {
                ProfileScreen(
                    onGoToEditProfile = { navController.navigate(Routes.EditProfile.route) },
                    onGoToMyShifts    = { navController.navigate(Routes.MyShiftsFromProfile.route) },
                    onGoToSettings    = { navController.navigate(Routes.Settings.route) },
                    onGoToWallet      = { navController.navigate(Routes.Wallet.route) },
                    onGoToSkills      = { navController.navigate(Routes.SkillsFromProfile.route) },
                    onLogout          = {
                        navController.navigate(Routes.Start.route) {
                            popUpTo(Routes.Main.route) { inclusive = true }
                        }
                    }
                )
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
                    onShiftClick = { shiftId ->
                        navController.navigate(Routes.ShiftSummary.createRoute(shiftId))
                    },
                    onBack = { navController.popBackStack() }
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
private fun ChambaPeBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest   = backStackEntry?.destination

    NavigationBar {
        MainTab.entries.forEach { tab ->
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
                icon  = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}