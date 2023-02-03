package com.mw.dogwellbeingtracker.presentation.common

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mw.dogwellbeingtracker.R
import com.mw.dogwellbeingtracker.presentation.bottom_bar_screens.home.HomeViewModel
import com.mw.dogwellbeingtracker.presentation.common.ExpandedMenuItemData.ExpandedMenuItem
import com.mw.dogwellbeingtracker.presentation.common.ExpandedMenuItemData.expandedMenuItemList
import com.mw.dogwellbeingtracker.presentation.top_bar_screens.dog_information.DogInformationViewModel
import com.mw.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens
import kotlinx.coroutines.launch

/**
 * TopAppBar composable that displays on all screens with back stack navigation.
 * Has a drop down menu with navigation to "DogInformationScreen" and "AddDogScreen"
 * If [selectedDogUiState] has a selected dog, their picture will display to the left of the drop down menu icon.
 * Users can tap on their dog's icon to bring down another drop down menu, containing a list of their dogs--tapping on a dog updates [selectedDogUiState] accordingly.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComposable(
    currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onExpandedMenuItemClick: (DogWellbeingTrackerScreens) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    dogInformationViewModel: DogInformationViewModel = hiltViewModel()
) {
    val selectedDogUiState by selectedDogUiState.collectAsState()
    val dogInformationUiState by dogInformationViewModel.dogInformationUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = context.getString(currentScreenTitle)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {
                    if (dogInformationUiState.dogList.isEmpty()) {
                        Toast.makeText(
                            context,
                            R.string.you_must_add_a_dog,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (currentScreenTitle == DogWellbeingTrackerScreens.DogInformation.title) {
                        navigateUp()
                        coroutineScope.launch { dogInformationViewModel.resetEditFieldExpansion() }
                    } else {
                        navigateUp()
                    }

                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            var dogsExpanded by rememberSaveable { mutableStateOf(false) }
            if (dogInformationUiState.dogList.isEmpty()) {
                /* Do Nothing */
            } else {
                AsyncImage(
                    model = selectedDogUiState.selectedDog.picture,
                    contentDescription = selectedDogUiState.selectedDog.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { dogsExpanded = !dogsExpanded }
                )
                DropdownMenu(
                    expanded = dogsExpanded,
                    onDismissRequest = { dogsExpanded = !dogsExpanded },
                    modifier = Modifier.width(180.dp)
                ) {
                    for (expandedDogItem in dogInformationUiState.dogList) {
                        DropdownMenuItem(text = { Text(text = expandedDogItem.name) },
                            onClick = {
                                coroutineScope.launch { homeViewModel.selectDog(expandedDogItem) }; dogsExpanded =
                                !dogsExpanded
                            },
                            leadingIcon =
                            {
                                AsyncImage(
                                    model = expandedDogItem.picture,
                                    contentDescription = expandedDogItem.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(30.dp)
                                )
                            })
                    }
                }
            }
            var menuExpanded by rememberSaveable { mutableStateOf(false) }
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.dropdown_menu)
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = !menuExpanded },
                modifier = Modifier.width(180.dp)
            ) {
                for (expandedMenuItem in expandedMenuItemList) {
                    DropdownMenuItem(
                        onClick = {
                            onExpandedMenuItemClick(expandedMenuItem.navigationDestination)
                            menuExpanded = !menuExpanded
                        },
                        text = { Text(text = stringResource(expandedMenuItem.text)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = expandedMenuItem.icon),
                                contentDescription = stringResource(expandedMenuItem.text)
                            )
                        }
                    )
                }
            }
        }
    )
}

/**
 * Object containing [ExpandedMenuItem] used in [expandedMenuItemList] for the top app bar's expanded menu.
 */
private object ExpandedMenuItemData {
    data class ExpandedMenuItem(
        val navigationDestination: DogWellbeingTrackerScreens,
        val icon: Int,
        @StringRes val text: Int,
    )

    val expandedMenuItemList = listOf(
        ExpandedMenuItem(
            DogWellbeingTrackerScreens.AddDog,
            R.drawable.ic_baseline_add_24,
            R.string.add_dog
        ),
        ExpandedMenuItem(
            DogWellbeingTrackerScreens.DogInformation,
            R.drawable.ic_baseline_dog_24,
            R.string.dog_information
        )
    )
}