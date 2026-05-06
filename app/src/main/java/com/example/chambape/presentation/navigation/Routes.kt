package com.example.chambape.presentation.navigation

sealed class Routes(val route: String) {
    // Auth
    object Start    : Routes("start")
    object Login    : Routes("login")
    object Register : Routes("register")
    object Skills   : Routes("skills")

    // Main container
    object Main : Routes("main")

    // Home tab
    object HomeFeed    : Routes("home_feed")
    object JobDetails  : Routes("job_details/{jobId}") {
        fun createRoute(jobId: String) = "job_details/$jobId"
    }
    object Apply       : Routes("apply/{jobId}") {
        fun createRoute(jobId: String) = "apply/$jobId"
    }
    object ActiveShift : Routes("active_shift")

    // Shifts tab
    object MyShifts     : Routes("my_shifts")
    object ShiftSummary : Routes("shift_summary/{shiftId}") {
        fun createRoute(shiftId: String) = "shift_summary/$shiftId"
    }

    // Messages tab
    object Messages : Routes("messages")
    object Chat     : Routes("chat/{chatId}") {
        fun createRoute(chatId: String) = "chat/$chatId"
    }

    // Profile tab
    object Profile : Routes("profile")
}
