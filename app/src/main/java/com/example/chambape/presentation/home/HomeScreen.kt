package com.example.chambape.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeFeedScreen(
    onJobClick: (jobId: String) -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Feed Screen")
    }
}

@Composable
fun JobDetailsScreen(
    jobId: String,
    onBack: () -> Unit,
    onApply: (jobId: String) -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Job Details — $jobId")
    }
}

@Composable
fun ApplyScreen(
    jobId: String,
    onConfirmed: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Apply Screen — $jobId")
    }
}

@Composable
fun ActiveShiftScreen(
    onClose: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Active Shift Screen")
    }
}
