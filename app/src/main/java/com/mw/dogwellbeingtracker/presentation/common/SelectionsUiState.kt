package com.mw.dogwellbeingtracker.presentation.common

import com.mw.dogwellbeingtracker.domain.model.Dog
import com.mw.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//This file contains the UI state used by "BottomAppBarComposable", as well as UI state for the selected dog.
// Holding states this way and having the "updateSelectedTab" function here are not ideal practices--will change later.

/**
 * Mutable state flow for [SelectedTabUiState].
 */
private val _selectedTabUiState = MutableStateFlow(SelectedTabUiState())

/**
 * Observable state flow for [SelectedTabUiState].
 */
val selectedTabUiState = _selectedTabUiState.asStateFlow()

/**
 * UI state for the selected tab.
 */
data class SelectedTabUiState(
    var selectedTab: DogWellbeingTrackerScreens = DogWellbeingTrackerScreens.Home
)

/**
 * Updates the selected tab to be used for dynamic navigation.
 */
fun updateSelectedTab(dogWellbeingTrackerScreen: DogWellbeingTrackerScreens) {
    _selectedTabUiState.value.selectedTab = dogWellbeingTrackerScreen
}

/**
 * Mutable state flow for [SelectedDogUiState].
 */
val _selectedDogUiState = MutableStateFlow(SelectedDogUiState())

/**
 * Observable state flow for [SelectedDogUiState].
 */
val selectedDogUiState = _selectedDogUiState.asStateFlow()

/**
 * UI state for the selected dog.
 */
data class SelectedDogUiState(
    val selectedDog: Dog = Dog()
)




