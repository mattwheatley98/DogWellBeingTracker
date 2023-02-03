package com.mw.dogwellbeingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.mw.dogwellbeingtracker.presentation.DogWellbeingTrackerApp
import com.mw.dogwellbeingtracker.theme.DogWellbeingTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogWellbeingTrackerTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                DogWellbeingTrackerApp(windowSize = windowSize.widthSizeClass)
            }
        }
    }
}
