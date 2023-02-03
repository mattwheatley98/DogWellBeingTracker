package com.mw.dogwellbeingtracker.presentation.util

import com.mw.dogwellbeingtracker.R

/**
 * Contains screen names for navigation, as well as their accompanying title for the top app bar.
 */
enum class DogWellbeingTrackerScreens(val title: Int) {
    BathroomTracker(R.string.bathroom_tracker),
    FoodTracker(R.string.food_tracker),
    Home(R.string.home),
    WalkTracker(R.string.walk_tracker),

    BathroomTrackerLog(R.string.comprehensive_bathroom_log),
    FoodTrackerLog(R.string.comprehensive_food_log),
    WalkTrackerLog(R.string.comprehensive_walk_log),

    DogInformation(R.string.dog_information),
    EditDogInformation(R.string.edit_dog_information),
    AddDog(R.string.add_dog),
    Settings(R.string.settings)
}