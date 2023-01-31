package com.example.dogwellbeingtracker.presentation.bottom_bar_screens.food_tracker

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.domain.model.Food
import com.example.dogwellbeingtracker.presentation.common.*
import com.example.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private object FoodTab {
    val currentTab = DogWellbeingTrackerScreens.FoodTracker
}

/**
 * Screen for the food tracker.
 */
@Composable
fun FoodTrackerScreen(
    onComprehensiveLogClick: () -> Unit,
    viewModel: FoodTrackerViewModel = hiltViewModel()
) {
    updateSelectedTab(FoodTab.currentTab); Log.d(ContentValues.TAG, "${FoodTab.currentTab}")
    val coroutineScope = rememberCoroutineScope()
    val selectedDogUiState by selectedDogUiState.collectAsState()
    val foodListUiState by viewModel.foodListUiState.collectAsState()

    FoodTrackerBody(
        onEnterFoodClick = { coroutineScope.launch { viewModel.addFood(it.toFood()) } },
        onFoodEntryClick = { coroutineScope.launch { viewModel.deleteFood(it) } },
        onFoodValueChange = viewModel::updateUiState,
        foodList = foodListUiState.foodList.filter {
            it.dogId == selectedDogUiState.selectedDog.id && it.date == DateTimeFormatter.ofPattern(
                "MM/dd/yy"
            ).format(LocalDate.now())
        },
        onComprehensiveLogClick = onComprehensiveLogClick,
        foodTrackerUiState = viewModel.foodTrackerUiState,
        selectedDogUiState = selectedDogUiState,
    )
}

/**
 * Body for [FoodTrackerScreen].
 * "DailyFoodLog" and "ComprehensiveLog" come from common.
 */
@Composable
private fun FoodTrackerBody(
    onEnterFoodClick: (FoodDetails) -> Unit,
    onFoodEntryClick: (Food) -> Unit,
    onFoodValueChange: (FoodDetails) -> Unit,
    foodList: List<Food>,
    onComprehensiveLogClick: () -> Unit,
    foodTrackerUiState: FoodTrackerUiState,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        EnterFood(
            onValueChange = onFoodValueChange,
            foodDetails = foodTrackerUiState.foodDetails,
            onEnterFoodClick = onEnterFoodClick,
            selectedDogUiState = selectedDogUiState
        )
        DailyFoodLog(
            foodList = foodList,
            onDataTypeEntryClick = { onFoodEntryClick(it) },
            selectedDogUiState = selectedDogUiState
        )
        DailyFoodSummary(
            selectedDogUiState = selectedDogUiState,
        )
        ComprehensiveLogText(
            logName = stringResource(R.string.food),
            onComprehensiveLogClick = { onComprehensiveLogClick() })
    }
}

/**
 * Composable that contains the food input fields.
 * [DateField], [TimeField], [TrackerTextField], [TypeField], and [EnterButton] are from common.
 */
@Composable
private fun EnterFood(
    onValueChange: (FoodDetails) -> Unit,
    foodDetails: FoodDetails,
    onEnterFoodClick: (FoodDetails) -> Unit,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.enter_in)} ${selectedDogUiState.selectedDog.name}${stringResource(id = R.string.s_meals_below)}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.align((Alignment.CenterHorizontally))
            )
            Row {
                DateField(
                    label = stringResource(id = R.string.date),
                    value = foodDetails.date,
                    onValueChange = { onValueChange(foodDetails.copy(date = it)) },
                    modifier = modifier.weight(.5F)
                )
                Spacer(modifier = modifier.padding(4.dp))
                TrackerTextField(
                    label = stringResource(id = R.string.notes),
                    value = foodDetails.notes,
                    onValueChange = { onValueChange(foodDetails.copy(notes = it)) },
                    keyboardType = KeyboardType.Text,
                    modifier = modifier.weight(.5F)
                )
            }
            Row {
                TrackerTextField(
                    label = stringResource(id = R.string.calories),
                    value = foodDetails.calories,
                    onValueChange = {
                        onValueChange(
                            foodDetails.copy(
                                calories = it,
                                dogId = selectedDogUiState.selectedDog.id
                            )
                        )
                    },
                    keyboardType = KeyboardType.Number,
                    isError = foodDetails.calories == "",
                    modifier = modifier.weight(.5F)
                )
                Spacer(modifier = modifier.padding(4.dp))
                TypeField(
                    label = stringResource(id = R.string.type),
                    value = foodDetails.type,
                    onValueChange = {
                        onValueChange(
                            foodDetails.copy(
                                type = it,
                                dogId = selectedDogUiState.selectedDog.id
                            )
                        )
                    },
                    isError = foodDetails.type == "",
                    list = listOf(
                        stringResource(R.string.snack),
                        stringResource(R.string.breakfast),
                        stringResource(R.string.lunch),
                        stringResource(R.string.dinner)
                    ),
                    modifier = modifier.weight(.5F)
                )
            }
            Spacer(modifier = modifier)
            EnterButton(
                detailsValue = foodDetails,
                entryDate = foodDetails.date,
                isError = foodDetails.calories == "" || foodDetails.type == "",
                dataTypeName = stringResource(id = R.string.food),
                onEnterClick = { onEnterFoodClick(it) },
                buttonText = stringResource(R.string.enter_food),
            )
        }
    }
}

/**
 * Composable that provides a visual summary about daily food consumption.
 * Also used in "HomeScreen".
 */
@Composable
fun DailyFoodSummary(
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Card() {
        val dailyCurrentCalories =
            selectedDogUiState.selectedDog.dailyCurrentCalories.toFloatOrNull()
                ?: 0.0F //Not an ideal practice--will change later.
        val dailyMaxCalories = selectedDogUiState.selectedDog.dailyMaxCalories.toFloatOrNull()
            ?: 0.0001F //Not an ideal practice--will change later.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
        ) {
            Text(text = stringResource(R.string.daily_calorie_meter), textAlign = TextAlign.Center)
            LinearProgressIndicator(
                progress = dailyCurrentCalories / dailyMaxCalories, //Not an ideal practice--will change later.
                color = if (dailyCurrentCalories / dailyMaxCalories > 1.0F) { //Not an ideal practice--will change later.
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .border(1.dp, color = MaterialTheme.colorScheme.primary)
            )
            Row(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${stringResource(id = R.string.current_colon)} ${selectedDogUiState.selectedDog.dailyCurrentCalories}"
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${stringResource(id = R.string.max_colon)} ${selectedDogUiState.selectedDog.dailyMaxCalories}",
                    )
                }
            }
        }
    }
}