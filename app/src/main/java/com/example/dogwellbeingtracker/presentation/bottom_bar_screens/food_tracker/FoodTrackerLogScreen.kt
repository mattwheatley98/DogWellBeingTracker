package com.example.dogwellbeingtracker.presentation.bottom_bar_screens.food_tracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.domain.model.Food
import com.example.dogwellbeingtracker.presentation.common.LogColumnTitle
import com.example.dogwellbeingtracker.presentation.common.LogEntry
import com.example.dogwellbeingtracker.presentation.common.selectedDogUiState
import kotlinx.coroutines.launch

/**
 * Screen for the comprehensive food log.
 */
@Composable
fun FoodTrackerLogScreen(viewModel: FoodTrackerViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val foodListUiState by viewModel.foodListUiState.collectAsState()
    val selectedDogUiState by selectedDogUiState.collectAsState()

    CalorieTrackerLogBody(
        onFoodEntryClick = { coroutineScope.launch { viewModel.deleteFood(it) } },
        foodList = foodListUiState.foodList.filter { it.dogId == selectedDogUiState.selectedDog.id },
    )
}

/**
 * Body for [FoodTrackerLogScreen].
 */
@Composable
private fun CalorieTrackerLogBody(
    onFoodEntryClick: (Food) -> Unit,
    foodList: List<Food>,
) {
    ComprehensiveFoodLog(
        foodList = foodList,
        onDataTypeEntryClick = { onFoodEntryClick(it) },
    )
}

/**
 * Comprehensive food log composable.
 * [LogColumnTitle] and [LogEntry] are from common.
 */
@Composable
private fun ComprehensiveFoodLog(
    foodList: List<Food>,
    onDataTypeEntryClick: (Food) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            LogColumnTitle(title = stringResource(id = R.string.date), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(R.string.calories), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.type), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.notes), modifier.weight(.25F))
        }
        LazyColumn {
            items(
                items = foodList,
                key = { food -> food.id }) { dataType ->
                LogEntry(
                    dataType = dataType,
                    entryValue1 = dataType.date,
                    entryValue2 = dataType.calories,
                    entryValue3 = dataType.type,
                    entryValue4 = dataType.notes,
                    onFoodEntryClick = { onDataTypeEntryClick(dataType) }
                )
            }
        }
    }
}