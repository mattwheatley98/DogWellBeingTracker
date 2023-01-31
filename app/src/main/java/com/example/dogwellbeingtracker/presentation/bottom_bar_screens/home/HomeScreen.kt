package com.example.dogwellbeingtracker.presentation.bottom_bar_screens.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.domain.model.Bathroom
import com.example.dogwellbeingtracker.domain.model.Dog
import com.example.dogwellbeingtracker.domain.model.Walk
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.bathroom_tracker.BathroomTrackerViewModel
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.bathroom_tracker.DailyBathroomSummary
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.food_tracker.DailyFoodSummary
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.walk_tracker.DailyWalkSummary
import com.example.dogwellbeingtracker.presentation.bottom_bar_screens.walk_tracker.WalkTrackerViewModel
import com.example.dogwellbeingtracker.presentation.common.SelectedDogUiState
import com.example.dogwellbeingtracker.presentation.common.selectedDogUiState
import com.example.dogwellbeingtracker.presentation.common.updateSelectedTab
import com.example.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private object HomeTab {
    val currentTab = DogWellbeingTrackerScreens.Home
}

/**
 * Home screen that initializes [DogListUiState] and contains a daily summary for each tracker.
 * If [DogListUiState] is empty, [DogUiState] will switch to [DogUiState.NoDogs] and [EmptyListAnimation] will prompt users to add a dog.
 */
@Composable
fun HomeScreen(
    addDog: () -> Unit,
    bathroomTrackerViewModel: BathroomTrackerViewModel = hiltViewModel(),
    walkTrackerViewModel: WalkTrackerViewModel = hiltViewModel(),
    viewModel: HomeViewModel = hiltViewModel(),
) {
    updateSelectedTab(HomeTab.currentTab); Log.d(TAG, "${HomeTab.currentTab}")
    val coroutineScope = rememberCoroutineScope()
    val dogListUiState by viewModel.dogListUiState.collectAsState()
    val selectedDogUiState by selectedDogUiState.collectAsState()
    val bathroomListUiState by bathroomTrackerViewModel.bathroomListUiState.collectAsState()
    val walkListUiState by walkTrackerViewModel.walkListUiState.collectAsState()

    when (viewModel.dogUiState) {
        is DogUiState.Loading -> LoadingScreen()
        is DogUiState.Success -> HomeBody(
            dogList = dogListUiState.dogList,
            onDogSelectionClick = { coroutineScope.launch { viewModel.selectDog(it) } },
            selectedDogUiState = selectedDogUiState,
            bathroomList = bathroomListUiState.bathroomList.filter {
                it.dogId == selectedDogUiState.selectedDog.id && it.date == DateTimeFormatter.ofPattern(
                    "MM/dd/yy"
                ).format(LocalDate.now())
            },
            walkList = walkListUiState.walkList.filter {
                it.dogId == selectedDogUiState.selectedDog.id && it.date == DateTimeFormatter.ofPattern(
                    "MM/dd/yy"
                ).format(LocalDate.now())
            },
            addDog = addDog,
        )
        else -> EmptyListAnimation(addDog)
    }
}

/**
 * Body for [HomeScreen].
 * If [selectedDogUiState] is empty (indicated if the selected dog's sex being "") [LoadingScreen] will appear.
 */
@Composable
private fun HomeBody(
    dogList: List<Dog>,
    bathroomList: List<Bathroom>,
    walkList: List<Walk>,
    onDogSelectionClick: (Dog) -> Unit,
    addDog: () -> Unit,      //Needed the the HomeScreen composable
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    if (selectedDogUiState.selectedDog.sex == "") {
        LoadingScreen()
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            CurrentDogs(
                dogList = dogList,
                onDogSelectionClick = onDogSelectionClick,
                selectedDogUiState = selectedDogUiState
            )
            DogSummary(
                bathroomList = bathroomList,
                walkList = walkList,
                selectedDogUiState = selectedDogUiState,
            )
        }
    }
}

/**
 * Composable that displays all dogs in a lazy row.
 * Can select a dog by tapping on their picture.
 */
@Composable
private fun CurrentDogs(
    dogList: List<Dog>,
    onDogSelectionClick: (Dog) -> Unit,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            items(items = dogList, key = { dog -> dog.id }) { dog ->
                CurrentDogsEntry(
                    dog = dog,
                    onDogSelectionClick = onDogSelectionClick,
                    selectedDogUiState = selectedDogUiState
                )
            }
        }
        Spacer(modifier = modifier.padding(4.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = "${selectedDogUiState.selectedDog.name}${stringResource(id = R.string.s_daily_summary)}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Single entry for a dog. Displayed in [CurrentDogs].
 */
@Composable
private fun CurrentDogsEntry(
    dog: Dog,
    onDogSelectionClick: (Dog) -> Unit,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Card(modifier = if (selectedDogUiState.selectedDog.name == dog.name) {
        modifier
            .size(125.dp)
            .padding(start = 10.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onDogSelectionClick(dog) }
    } else {
        modifier
            .size(125.dp)
            .padding(start = 10.dp)
            .clickable { onDogSelectionClick(dog) }
    }
    ) {
        AsyncImage(
            model = if (selectedDogUiState.selectedDog.sex == "") {
                R.drawable.icons8_dog_100
            } else {
                dog.picture
            },
            contentDescription = dog.name,
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * Loading screen when [DogUiState.Loading].
 */
@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

/**
 * Displays when [DogUiState.NoDogs] and prompts users to add a dog.
 * Also used in "DogInformationScreen" when the dog list is empty.
 */
@Composable
fun EmptyListAnimation(addDog: () -> Unit, modifier: Modifier = Modifier) {
    val visibleState =
        remember {
            MutableTransitionState(initialState = false).apply {
                targetState = true
            }
        }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = tween(
                1000,
                easing = LinearEasing
            )
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(1.dp)
        ) {
            Card(modifier = modifier.padding(16.dp)) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.no_dogs_found_please_add),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = modifier.padding(4.dp))
                    Button(onClick = { addDog() }) {
                        Text(text = stringResource(id = R.string.add_dog))
                    }
                }
            }
        }
    }
}

/**
 * Composable that displays [DailyBathroomSummary], [DailyFoodSummary], and [DailyWalkSummary].
 */
@Composable
private fun DogSummary(
    bathroomList: List<Bathroom>,
    walkList: List<Walk>,
    selectedDogUiState: SelectedDogUiState,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DailyBathroomSummary(bathroomList = bathroomList)
        DailyFoodSummary(
            selectedDogUiState = selectedDogUiState,
        )
        DailyWalkSummary(selectedDogUiState = selectedDogUiState, walkList = walkList)
    }
}