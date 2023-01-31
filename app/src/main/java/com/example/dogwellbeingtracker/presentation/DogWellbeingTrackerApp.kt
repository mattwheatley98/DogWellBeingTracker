package com.example.dogwellbeingtracker.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.bathroom_tracker.BathroomTrackerLogScreen
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.bathroom_tracker.BathroomTrackerScreen
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.food_tracker.FoodTrackerLogScreen
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.food_tracker.FoodTrackerScreen
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.home.HomeScreen
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.walk_tracker.WalkTrackerLogScreen
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.walk_tracker.WalkTrackerScreen
import com.example.dogwellbeingtracker.presentation.common.*
import com.example.dogwellbeingtracker.presentation.top_bar_screens.add_dog.AddDogScreen
import com.example.dogwellbeingtracker.presentation.top_bar_screens.dog_information.DogInformationScreen
import com.example.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogWellbeingTrackerApp(windowSize: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = DogWellbeingTrackerScreens.valueOf(
        value = backStackEntry?.destination?.route ?: DogWellbeingTrackerScreens.Home.name
    )
    val navHost = remember {
        movableContentOf<PaddingValues> { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = DogWellbeingTrackerScreens.Home.name,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                composable(route = DogWellbeingTrackerScreens.BathroomTracker.name) {
                    BathroomTrackerScreen(onComprehensiveLogClick = {
                        navController.navigate(
                            DogWellbeingTrackerScreens.BathroomTrackerLog.name
                        )
                    })
                }
                composable(route = DogWellbeingTrackerScreens.BathroomTrackerLog.name) {
                    BathroomTrackerLogScreen()
                }
                composable(route = DogWellbeingTrackerScreens.FoodTracker.name) {
                    FoodTrackerScreen(onComprehensiveLogClick = {
                        navController.navigate(
                            DogWellbeingTrackerScreens.FoodTrackerLog.name
                        )
                    })
                }
                composable(route = DogWellbeingTrackerScreens.FoodTrackerLog.name) {
                    FoodTrackerLogScreen()
                }
                composable(route = DogWellbeingTrackerScreens.Home.name) {
                    HomeScreen(addDog = { navController.navigate(DogWellbeingTrackerScreens.AddDog.name) })
                }
                composable(route = DogWellbeingTrackerScreens.WalkTracker.name) {
                    WalkTrackerScreen(onComprehensiveLogClick = {
                        navController.navigate(
                            DogWellbeingTrackerScreens.WalkTrackerLog.name
                        )
                    })
                }
                composable(route = DogWellbeingTrackerScreens.WalkTrackerLog.name) {
                    WalkTrackerLogScreen()
                }
                composable(route = DogWellbeingTrackerScreens.DogInformation.name) {
                    DogInformationScreen(addDog = {
                        navController.navigate(
                            DogWellbeingTrackerScreens.AddDog.name
                        )
                    }, windowSize = windowSize)
                }
                composable(route = DogWellbeingTrackerScreens.AddDog.name) {
                    AddDogScreen(
                        onToHomeClick = { navController.navigate(DogWellbeingTrackerScreens.Home.name) },
                        addDog = { navController.navigate(DogWellbeingTrackerScreens.Home.name) })
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarComposable(
                currentScreenTitle = currentScreen.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp(); },
                onExpandedMenuItemClick = { navController.navigate(it.name) },
            )
        },
    ) { innerPadding ->
        when (windowSize) {
            WindowWidthSizeClass.Compact -> {
                BottomBarLayout(
                    currentScreen = currentScreen.title,
                    onDynamicNavigationClick = { navController.navigate(it.name) },
                ) {
                    navHost(PaddingValues(bottom = 64.dp, top = innerPadding.calculateTopPadding()))
                }
            }
            WindowWidthSizeClass.Medium -> {
                NavigationRailLayout(
                    currentScreen = currentScreen.title,
                    onDynamicNavigationClick = { navController.navigate(it.name) },
                    paddingValues = innerPadding,
                ) {
                    navHost(PaddingValues())
                }
            }
            WindowWidthSizeClass.Expanded -> {
                PermanentNavigationDrawerLayout(
                    currentScreen = currentScreen.title,
                    onDynamicNavigationClick = { navController.navigate(it.name) },
                    paddingValues = innerPadding,
                ) {
                    navHost(PaddingValues())
                }
            }
        }

    }
}