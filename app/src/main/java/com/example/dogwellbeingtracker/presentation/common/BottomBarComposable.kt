package com.example.dogwellbeingtracker.presentation.common

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.presentation.common.DynamicNavigationItemData.DynamicNavigationItem
import com.example.dogwellbeingtracker.presentation.common.DynamicNavigationItemData.dynamicNavigationItemList
import com.example.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens

/**
 * BottomAppBar composable that displays on all screens, except for "AddDogScreen" and "DogInformationScreen".
 * If no dog is in [selectedDogUiState], navigation will not function.
 */
@Composable
fun BottomBarComposable(
    onDynamicNavigationClick: (DogWellbeingTrackerScreens) -> Unit,
    selectedTabUiState: SelectedTabUiState,
) {
    val selectedDogUiState by selectedDogUiState.collectAsState()
    val context = LocalContext.current

    BottomAppBar {
        for (navItem in dynamicNavigationItemList) {
            NavigationBarItem(
                selected = navItem.navigationDestination == selectedTabUiState.selectedTab,
                onClick = {
                    if (selectedDogUiState.selectedDog.sex == "") {
                        Toast.makeText(
                            context,
                            R.string.you_must_add_a_dog,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onDynamicNavigationClick(navItem.navigationDestination)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.icon),
                        contentDescription = navItem.navigationDestination.title.toString()
                    )
                }
            )
        }
    }
}

/**
 * Object containing [DynamicNavigationItem] used in [dynamicNavigationItemList] for the bottom app bar's icons.
 */
private object DynamicNavigationItemData {
    data class DynamicNavigationItem(
        var navigationDestination: DogWellbeingTrackerScreens,
        @DrawableRes var icon: Int,
    )

    val dynamicNavigationItemList = listOf(
        DynamicNavigationItem(
            DogWellbeingTrackerScreens.Home,
            R.drawable.ic_baseline_home_24,
        ),
        DynamicNavigationItem(
            DogWellbeingTrackerScreens.BathroomTracker,
            R.drawable.ic_baseline_pets_24,
        ),
        DynamicNavigationItem(
            DogWellbeingTrackerScreens.FoodTracker,
            R.drawable.ic_baseline_fastfood_24,
        ),
        DynamicNavigationItem(
            DogWellbeingTrackerScreens.WalkTracker,
            R.drawable.ic_baseline_directions_walk_24,
        ),
    )
}