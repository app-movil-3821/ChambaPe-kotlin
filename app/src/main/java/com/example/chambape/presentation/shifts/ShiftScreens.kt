package com.example.chambape.presentation.shifts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MyShiftsScreen(
    onShiftClick: (shiftId: String) -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("My Shifts Screen")
    }
}

@Composable
fun ShiftSummaryScreen(
    shiftId: String,
    onDone: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Shift Summary — $shiftId")
    }
}
