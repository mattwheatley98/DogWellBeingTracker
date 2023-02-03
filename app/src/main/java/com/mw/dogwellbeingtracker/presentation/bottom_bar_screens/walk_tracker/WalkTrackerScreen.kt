package com.mw.dogwellbeingtracker.presentation.bottom_bar_screens.walk_tracker

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mw.dogwellbeingtracker.R
import com.mw.dogwellbeingtracker.domain.model.Walk
import com.mw.dogwellbeingtracker.presentation.common.*
import com.mw.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private object WalkTab {
    val currentTab = DogWellbeingTrackerScreens.WalkTracker
}

/**
 * Screen for the walk tracker.
 */
@Composable
fun WalkTrackerScreen(
    onComprehensiveLogClick: () -> Unit,
    viewModel: WalkTrackerViewModel = hiltViewModel()
) {
    updateSelectedTab(WalkTab.currentTab); Log.d(ContentValues.TAG, "${WalkTab.currentTab}")
    val coroutineScope = rememberCoroutineScope()
    val selectedDogUiState by selectedDogUiState.collectAsState()
    val walkListUiState by viewModel.walkListUiState.collectAsState()

    WalkTrackerBody(
        onEnterWalkClick = { coroutineScope.launch { viewModel.addWalk() } },
        onWalkEntryClick = { coroutineScope.launch { viewModel.deleteWalk(it) } },
        onWalkValueChange = viewModel::updateUiState,
        walkList = walkListUiState.walkList.filter {
            it.dogId == selectedDogUiState.selectedDog.id && it.date == DateTimeFormatter.ofPattern(
                "MM/dd/yy"
            ).format(LocalDate.now())
        },
        onComprehensiveLogClick = onComprehensiveLogClick,
        walkTrackerUiState = viewModel.walkTrackerUiState,
        selectedDogUiState = selectedDogUiState
    )
}

/**
 * Body for [WalkTrackerScreen].
 * [DailyWalkLog] and [ComprehensiveLogText] are from common.
 */
@Composable
private fun WalkTrackerBody(
    onEnterWalkClick: (WalkDetails) -> Unit,
    onWalkEntryClick: (Walk) -> Unit,
    onWalkValueChange: (WalkDetails) -> Unit,
    walkList: List<Walk>,
    onComprehensiveLogClick: () -> Unit,
    walkTrackerUiState: WalkTrackerUiState,
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
        EnterWalk(
            onValueChange = onWalkValueChange,
            onEnterWalkClick = onEnterWalkClick,
            walkDetails = walkTrackerUiState.walkDetails,
            selectedDogUiState = selectedDogUiState
        )
        DailyWalkLog(
            walkList = walkList,
            onDataTypeEntryClick = { onWalkEntryClick(it) },
        )
        DailyWalkSummary(selectedDogUiState = selectedDogUiState, walkList = walkList)
        ComprehensiveLogText(
            logName = stringResource(R.string.walk),
            onComprehensiveLogClick = { onComprehensiveLogClick() })
    }
}

/**
 * Composable that contains the walk input fields.
 * [DateField], [TimeField], [DurationField], [TrackerTextField], and [EnterButton] are from common.
 */
@Composable
private fun EnterWalk(
    onValueChange: (WalkDetails) -> Unit,
    walkDetails: WalkDetails,
    onEnterWalkClick: (WalkDetails) -> Unit,
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
                text = "${stringResource(id = R.string.enter_in)} ${selectedDogUiState.selectedDog.name}${stringResource(id = R.string.s_walks_below)}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.align((Alignment.CenterHorizontally))
            )
            Row {
                DateField(
                    label = stringResource(id = R.string.date),
                    value = walkDetails.date,
                    onDateValueChange = { onValueChange(walkDetails.copy(date = it)) },
                    modifier = modifier.weight(.5F)
                )
                Spacer(modifier = modifier.padding(4.dp))
                TimeField(
                    label = stringResource(id = R.string.time),
                    value = walkDetails.time,
                    onTimeValueChange = { onValueChange(walkDetails.copy(time = it)) },
                    modifier = modifier.weight(.5F)
                )
            }
            Row {
                DurationField(
                    label = stringResource(id = R.string.duration),
                    value = walkDetails.duration,
                    onValueChange = {
                        onValueChange(
                            walkDetails.copy(
                                duration = it,
                                dogId = selectedDogUiState.selectedDog.id,
                            )
                        )
                    },
                    isError = walkDetails.duration == "",
                    modifier = modifier.weight(.5F)
                )
                Spacer(modifier = modifier.padding(4.dp))
                TrackerTextField(
                    label = stringResource(id = R.string.notes),
                    value = walkDetails.notes,
                    onValueChange = {
                        onValueChange(
                            walkDetails.copy(
                                notes = it,
                                dogId = selectedDogUiState.selectedDog.id,
                            )
                        )
                    },
                    keyboardType = KeyboardType.Text,
                    modifier = modifier.weight(.5F)
                )
            }
            Spacer(modifier = modifier)
            EnterButton(
                detailsValue = walkDetails,
                entryDate = walkDetails.date,
                isError = walkDetails.duration == "",
                dataTypeName = stringResource(id = R.string.walk),
                onEnterClick = { onEnterWalkClick(it) },
                buttonText = stringResource(R.string.enter_walk),
            )
        }
    }
}

/**
 * Composable that provides a summary about daily walks.
 * Also used in "HomeScreen".
 */
@Composable
fun DailyWalkSummary(
    walkList: List<Walk>,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Card() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = "${selectedDogUiState.selectedDog.name} ${stringResource(id = R.string.has_had)} ",
                )
                Text(
                    text = "${
                        walkList.filter {
                            it.date == DateTimeFormatter.ofPattern("MM/dd/yy").format(
                                LocalDate.now()
                            )
                        }.size
                    }",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (walkList.filter {
                            it.date == DateTimeFormatter.ofPattern("MM/dd/yy").format(
                                LocalDate.now()
                            )
                        }.size == 1) {
                        " ${stringResource(id = R.string.walk_today)}"
                    } else {
                        " ${stringResource(id = R.string.walks_today)}"
                    }
                )
            }
        }
    }
}