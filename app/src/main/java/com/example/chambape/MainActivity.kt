package com.example.chambape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.chambape.presentation.navigation.AppNavHost
import com.example.chambape.ui.theme.ChambaPeTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChambaPeTheme {
                AppNavHost()
            }
        }
    }
}
