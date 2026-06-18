package com.example.chambape.presentation.navigation

sealed class Routes(val route: String) {

    // Auth
    object Start             : Routes("start")
    object Login             : Routes("login")
    object Register          : Routes("register")
    object PhoneVerification : Routes("phone_verification")
    object ProfileSetup      : Routes("profile_setup")
    object Skills            : Routes("skills")

    // Main container
    object Main : Routes("main")

    // Home tab
    object HomeFeed    : Routes("home_feed")
    object JobDetails  : Routes("job_details/{jobId}") {
        fun createRoute(jobId: String) = "job_details/$jobId"
    }
    object Apply       : Routes("apply/{jobId}/{contractorId}") {
        fun createRoute(jobId: String, contractorId: String) = "apply/$jobId/$contractorId"
    }
    object ActiveShift : Routes("active_shift/{jobId}") {
        fun createRoute(jobId: String) = "active_shift/$jobId"
    }
    object Help : Routes("help")

    object CreateJob : Routes("create_job")

    // Shifts tab
    object MyShifts            : Routes("my_shifts")
    object MyShiftsFromProfile : Routes("my_shifts_from_profile")  // ← desde Profile
    object ShiftSummary        : Routes("shift_summary/{shiftId}") {
        fun createRoute(shiftId: String) = "shift_summary/$shiftId"
    }

    // Messages tab
    object Messages : Routes("messages")
    object Chat     : Routes("chat/{conversationId}/{jobId}") {
        fun createRoute(conversationId: String, jobId: String) = "chat/$conversationId/$jobId"
    }

    // Profile tab
    object Notifications     : Routes("notifications")
    object Profile           : Routes("profile")
    object EditProfile       : Routes("edit_profile")
    object ChangePassword    : Routes("change_password")
    object Settings          : Routes("settings")
    object Wallet            : Routes("wallet")
    object SkillsFromProfile : Routes("skills_from_profile")  // ← desde Profile
}