package com.example.chambape.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME(
        route     = Routes.HomeFeed.route,
        label     = "Home",
        icon      = Icons.Outlined.Home
    ),
    SHIFTS(
        route     = Routes.MyShifts.route,
        label     = "Shifts",
        icon      = Icons.Outlined.WorkOutline
    ),
    MESSAGES(
        route     = Routes.Messages.route,
        label     = "Messages",
        icon      = Icons.Outlined.ChatBubbleOutline
    ),
    PROFILE(
        route     = Routes.Profile.route,
        label     = "Profile",
        icon      = Icons.Outlined.Person
    )
}
