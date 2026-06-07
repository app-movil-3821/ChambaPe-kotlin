package com.example.chambape.presentation.home.domain.model

// El modelo limpio que usará tu vista para pintar la información básica
data class Job(
    val id: String,
    val title: String,
    val description: String,
    val paymentAmount: Double,
    val district: String
)