package com.example.dogwellbeingtracker.presentation.bottom_bar_screens.bathroom_tracker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dogwellbeingtracker.R
import com.example.dogwellbeingtracker.domain.model.Bathroom
import com.example.dogwellbeingtracker.presentation.common.*
import com.example.dogwellbeingtracker.presentation.util.DogWellbeingTrackerScreens
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private object BathroomTab {
    val currentTab = DogWellbeingTrackerScreens.BathroomTracker
}

/**
 * Screen for the bathroom tracker.
 */
@Composable
fun BathroomTrackerScreen(
    onComprehensiveLogClick: () -> Unit,
    viewModel: BathroomTrackerViewModel = hiltViewModel()
) {
    updateSelectedTab(BathroomTab.currentTab)
    val coroutineScope = rememberCoroutineScope()
    val selectedDogUiState by selectedDogUiState.collectAsState()
    val bathroomListUiState by viewModel.bathroomListUiState.collectAsState()

    BathroomTrackerBody(
        onEnterBathroomClick = { coroutineScope.launch { viewModel.addBathroom() } },
        onBathroomEntryClick = { coroutineScope.launch { viewModel.deleteBathroom(it) } },
        onBathroomValueChange = viewModel::updateUiState,
        bathroomList = bathroomListUiState.bathroomList.filter {
            it.dogId == selectedDogUiState.selectedDog.id && it.date == DateTimeFormatter.ofPattern(
                "MM/dd/yy"
            ).format(LocalDate.now())
        },
        bathroomTrackerUiState = viewModel.bathroomTrackerUiState,
        onComprehensiveLogClick = onComprehensiveLogClick,
        selectedDogUiState = selectedDogUiState
    )
}

/**
 * Body for [BathroomTrackerScreen].
 * [DailyBathroomLog] and [ComprehensiveLogText] come from common.
 */
@Composable
private fun BathroomTrackerBody(
    onEnterBathroomClick: (BathroomDetails) -> Unit,
    onBathroomEntryClick: (Bathroom) -> Unit,
    onBathroomValueChange: (BathroomDetails) -> Unit,
    bathroomList: List<Bathroom>,
    onComprehensiveLogClick: () -> Unit,
    bathroomTrackerUiState: BathroomTrackerUiState,
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
        EnterBathroom(
            onValueChange = onBathroomValueChange,
            bathroomDetails = bathroomTrackerUiState.bathroomDetails,
            onEnterBathroomClick = onEnterBathroomClick,
            selectedDogUiState = selectedDogUiState
        )
        DailyBathroomLog(
            bathroomList = bathroomList,
            onDataTypeEntryClick = { onBathroomEntryClick(it) },
            selectedDogUiState = selectedDogUiState
        )
        DailyBathroomSummary(
            bathroomList = bathroomList
        )
        ComprehensiveLogText(
            logName = stringResource(R.string.bathroom),
            onComprehensiveLogClick = { onComprehensiveLogClick() })
    }
}

/**
 * Composable that contains the bathroom input fields.
 * [DateField], [TimeField], [TypeField], [TrackerTextField], and [EnterButton] are from common.
 */
@Composable
private fun EnterBathroom(
    onValueChange: (BathroomDetails) -> Unit,
    bathroomDetails: BathroomDetails,
    onEnterBathroomClick: (BathroomDetails) -> Unit,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.enter_in)} ${selectedDogUiState.selectedDog.name}${stringResource(id = R.string.s_bathroom_breaks_below)}",
                textAlign = TextAlign.Center,
                modifier = modifier.align((Alignment.CenterHorizontally))
            )
            Row {
                DateField(
                    label = stringResource(id = R.string.date),
                    value = bathroomDetails.date,
                    onValueChange = { onValueChange(bathroomDetails.copy(date = it)) },
                    modifier = modifier.weight(.5F)
                )
                Spacer(modifier = modifier.padding(4.dp))
                TimeField(
                    label = stringResource(id = R.string.time),
                    value = bathroomDetails.time,
                    onTimeValueChange = { onValueChange(bathroomDetails.copy(time = it)) },
                    modifier = modifier.weight(.5F)
                )
            }
            Row {
                TypeField(
                    label = stringResource(id = R.string.type),
                    value = bathroomDetails.type,
                    onValueChange = {
                        onValueChange(
                            bathroomDetails.copy(
                                type = it,
                                dogId = selectedDogUiState.selectedDog.id
                            )
                        )
                    },
                    isError = bathroomDetails.type == "",
                    list = listOf(stringResource(id = R.string.pee), stringResource(id = R.string.poop)),
                    modifier = modifier.weight(.5F)
                )
                Spacer(modifier = modifier.padding(4.dp))
                TrackerTextField(
                    label = stringResource(id = R.string.notes),
                    value = bathroomDetails.notes,
                    onValueChange = { onValueChange(bathroomDetails.copy(notes = it)) },
                    keyboardType = KeyboardType.Text,
                    modifier = modifier.weight(.5F)
                )
            }
            EnterButton(
                detailsValue = bathroomDetails,
                entryDate = bathroomDetails.date,
                isError = bathroomDetails.type == "",
                dataTypeName = stringResource(id = R.string.bathroom),
                onEnterClick = onEnterBathroomClick,
                buttonText = stringResource(R.string.enter_bathroom),
            )
        }
    }
}

/**
 * Composable that provides a summary about daily bathroom usage.
 * Also used in "HomeScreen"
 */
@Composable
fun DailyBathroomSummary(
    bathroomList: List<Bathroom>,
    modifier: Modifier = Modifier
) {
    Card() {
        Row(
            modifier = modifier
                .padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Card {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.times_peed))
                    Text(
                        text = "${
                            bathroomList.filter {
                                it.date == DateTimeFormatter.ofPattern("MM/dd/yy")
                                    .format(LocalDate.now()) && it.type == stringResource(R.string.pee)
                            }.size
                        }",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = modifier.fillMaxWidth()) {
                Card {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.times_pooped))
                        Text(
                            text = "${
                                bathroomList.filter {
                                    it.date == DateTimeFormatter.ofPattern("MM/dd/yy")
                                        .format(LocalDate.now()) && it.type == stringResource(R.string.poop)
                                }.size
                            }",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}