package com.mw.dogwellbeingtracker.presentation.common

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mw.dogwellbeingtracker.R
import com.mw.dogwellbeingtracker.presentation.common.DynamicNavigationItemData.DynamicNavigationItem
import com.mw.dogwellbeingtracker.presentation.common.DynamicNavigationItemData.dynamicNavigationItemList
import com.mw.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens

/**
 * BottomAppBar composable that displays on all screens, except for "AddDogScreen" and "DogInformationScreen".
 * Is used when WindowWidthSizeClass is Compact.
 * If no dog is in [selectedDogUiState], navigation will not function.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarLayout(
    @StringRes currentScreen: Int,
    onDynamicNavigationClick: (DogWellbeingTrackerScreens) -> Unit,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(bottomBar = {
        if (currentScreen == DogWellbeingTrackerScreens.AddDog.title || currentScreen == DogWellbeingTrackerScreens.DogInformation.title) {
            /* Do nothing */
        } else {
            BottomAppBar {
                val selectedDogUiState by selectedDogUiState.collectAsState()
                val context = LocalContext.current

                for (navItem in dynamicNavigationItemList) {
                    NavigationBarItem(
                        selected = navItem.navigationDestination == selectedTabUiState.value.selectedTab,
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
    })
    { innerPadding ->
        content(innerPadding)
    }
}

/**
 * NavigationRail composable that displays on all screens, except for "AddDogScreen" and "DogInformationScreen".
 * Is used when WindowWidthSizeClass is Medium.
 * If no dog is in [selectedDogUiState], navigation will not function.
 */
@Composable
fun NavigationRailLayout(
    @StringRes currentScreen: Int,
    onDynamicNavigationClick: (DogWellbeingTrackerScreens) -> Unit,
    paddingValues: PaddingValues,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
        if (currentScreen == DogWellbeingTrackerScreens.AddDog.title || currentScreen == DogWellbeingTrackerScreens.DogInformation.title) {
            /* Do nothing */
        } else {
            NavigationRail {
                val selectedDogUiState by selectedDogUiState.collectAsState()
                val context = LocalContext.current

                for (navItem in dynamicNavigationItemList) {
                    NavigationRailItem(
                        selected = navItem.navigationDestination == selectedTabUiState.value.selectedTab,
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
        content()
    }
}

/**
 * PermanentNavigationDrawer composable that displays on all screens, except for "AddDogScreen" and "DogInformationScreen".
 * Is used when WindowWidthSizeClass is Expanded.
 * If no dog is in [selectedDogUiState], navigation will not function.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentNavigationDrawerLayout(
    @StringRes currentScreen: Int,
    onDynamicNavigationClick: (DogWellbeingTrackerScreens) -> Unit,
    paddingValues: PaddingValues,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
        if (currentScreen == DogWellbeingTrackerScreens.AddDog.title || currentScreen == DogWellbeingTrackerScreens.DogInformation.title) {
            /* Do nothing */
        } else {
            PermanentNavigationDrawer(
                drawerContent = {},
                modifier = Modifier
                    .width(200.dp)
            ) {
                PermanentDrawerSheet(modifier = Modifier.width(200.dp)) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(12.dp)
                    ) {
                        val selectedDogUiState by selectedDogUiState.collectAsState()
                        val context = LocalContext.current

                        for (navItem in dynamicNavigationItemList) {
                            NavigationDrawerItem(
                                label = { Text(text = stringResource(navItem.navigationDestination.title)) },
                                selected = navItem.navigationDestination == selectedTabUiState.value.selectedTab,
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
            }
        }
        content()
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