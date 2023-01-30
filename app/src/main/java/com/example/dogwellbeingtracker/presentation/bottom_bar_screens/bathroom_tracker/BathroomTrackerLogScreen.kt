package com.example.dogwellbeingtracker.presentation.bottom_bar_screens.bathroom_tracker

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
import com.example.dogwellbeingtracker.domain.model.Bathroom
import com.example.dogwellbeingtracker.presentation.common.LogColumnTitle
import com.example.dogwellbeingtracker.presentation.common.LogEntry
import com.example.dogwellbeingtracker.presentation.common.selectedDogUiState
import kotlinx.coroutines.launch

/**
 * Screen for the comprehensive bathroom log.
 */
@Composable
fun BathroomTrackerLogScreen(viewModel: BathroomTrackerViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val bathroomListUiState by viewModel.bathroomListUiState.collectAsState()
    val selectedDogUiState by selectedDogUiState.collectAsState()

    BathroomTrackerLogBody(
        onBathroomEntryClick = { coroutineScope.launch { viewModel.deleteBathroom(it) } },
        bathroomList = bathroomListUiState.bathroomList.filter { it.dogId == selectedDogUiState.selectedDog.id },
    )
}

/**
 * Body for [BathroomTrackerLogScreen].
 */
@Composable
private fun BathroomTrackerLogBody(
    onBathroomEntryClick: (Bathroom) -> Unit,
    bathroomList: List<Bathroom>,
) {
    ComprehensiveBathroomLog(
        bathroomList = bathroomList,
        onDataTypeEntryClick = { onBathroomEntryClick(it) },
    )
}

/**
 * Comprehensive bathroom log composable.
 * [LogColumnTitle] and [LogEntry] are from common.
 */
@Composable
private fun ComprehensiveBathroomLog(
    bathroomList: List<Bathroom>,
    onDataTypeEntryClick: (Bathroom) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            LogColumnTitle(title = stringResource(R.string.date), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(R.string.time), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(R.string.type), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(R.string.notes), modifier.weight(.25F))
        }
        LazyColumn {
            items(
                items = bathroomList,
                key = { bathroom -> bathroom.id }) { dataType ->
                LogEntry(
                    dataType = dataType,
                    entryValue1 = dataType.date,
                    entryValue2 = dataType.time,
                    entryValue3 = dataType.type,
                    entryValue4 = dataType.notes,
                    onFoodEntryClick = { onDataTypeEntryClick(dataType) }
                )
            }
        }
    }
}