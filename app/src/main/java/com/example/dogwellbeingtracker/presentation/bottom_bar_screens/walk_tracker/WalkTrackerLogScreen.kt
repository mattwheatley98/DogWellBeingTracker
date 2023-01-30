package com.example.dogwellbeingtracker.presentation.bottom_bar_screens.walk_tracker

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
import com.example.dogwellbeingtracker.domain.model.Walk
import com.example.dogwellbeingtracker.presentation.common.LogColumnTitle
import com.example.dogwellbeingtracker.presentation.common.LogEntry
import com.example.dogwellbeingtracker.presentation.common.selectedDogUiState
import kotlinx.coroutines.launch

/**
 * Screen for the comprehensive walk log.
 */
@Composable
fun WalkTrackerLogScreen(
    viewModel: WalkTrackerViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val walkListUiState by viewModel.walkListUiState.collectAsState()
    val selectedDogUiState by selectedDogUiState.collectAsState()

    WalkTrackerLogBody(
        onWalkEntryClick = { coroutineScope.launch { viewModel.deleteWalk(it) } },
        walkList = walkListUiState.walkList.filter { it.dogId == selectedDogUiState.selectedDog.id },
    )
}

/**
 * Body for [WalkTrackerLogScreen].
 */
@Composable
private fun WalkTrackerLogBody(
    onWalkEntryClick: (Walk) -> Unit,
    walkList: List<Walk>,
) {
    ComprehensiveWalkLog(
        walkList = walkList,
        onDataTypeEntryClick = { onWalkEntryClick(it) },
    )
}

/**
 * Comprehensive walk log composable.
 * [LogColumnTitle] and [LogEntry] are from common.
 */
@Composable
private fun ComprehensiveWalkLog(
    walkList: List<Walk>,
    onDataTypeEntryClick: (Walk) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            LogColumnTitle(title = stringResource(id = R.string.date), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.start), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.duration), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.notes), modifier.weight(.25F))
        }
        LazyColumn {
            items(
                items = walkList,
                key = { walk -> walk.id }) { dataType ->
                LogEntry(
                    dataType = dataType,
                    entryValue1 = dataType.date,
                    entryValue2 = dataType.time,
                    entryValue3 = dataType.duration,
                    entryValue4 = dataType.notes,
                    onFoodEntryClick = { onDataTypeEntryClick(dataType) }
                )
            }
        }
    }
}