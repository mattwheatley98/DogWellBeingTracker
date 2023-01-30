package com.example.dogwellbeingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.dogwellbeingtracker.presentation.DogWellbeingTrackerApp
import com.example.dogwellbeingtracker.theme.DogWellbeingTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogWellbeingTrackerTheme {
                DogWellbeingTrackerApp()
            }
        }
    }
}
