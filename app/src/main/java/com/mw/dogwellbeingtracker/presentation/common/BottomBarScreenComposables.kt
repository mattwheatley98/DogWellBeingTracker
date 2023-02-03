package com.mw.dogwellbeingtracker.presentation.common

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.maxkeppeler.sheets.duration.DurationDialog
import com.maxkeppeler.sheets.duration.models.DurationConfig
import com.maxkeppeler.sheets.duration.models.DurationFormat
import com.maxkeppeler.sheets.duration.models.DurationSelection
import com.mw.dogwellbeingtracker.R
import com.mw.dogwellbeingtracker.domain.model.Bathroom
import com.mw.dogwellbeingtracker.domain.model.Food
import com.mw.dogwellbeingtracker.domain.model.Walk
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Reusable outlined text field displays a [CalendarDialog] when clicked.
 * Users can select a date either in the present, or retroactively.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    label: String,
    value: String,
    onDateValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val calendarState = rememberSheetState()

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            onDateValueChange(DateTimeFormatter.ofPattern("MM/dd/yy").format(date))
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            disabledTimeline = CalendarTimeline.FUTURE
        )
    )
    OutlinedTextField(
        value = value,
        onValueChange = { },
        singleLine = true,
        enabled = false,
        label = { Text(text = label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier
            .clickable { calendarState.show() }
            .fillMaxWidth()
    )
}

/**
 * Reusable outlined text field that displays a [ClockDialog] when clicked.
 * Users can select a time following an AM/PM format.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    label: String,
    value: String,
    onTimeValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clockState = rememberSheetState()

    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes { hour, minute ->
            val timeValue = LocalTime.of(hour, minute)
            onTimeValueChange(DateTimeFormatter.ofPattern("hh:mm a").format(timeValue))
        },
        config = ClockConfig(
            is24HourFormat = false, defaultTime = LocalTime.now()
        ),
    )
    OutlinedTextField(
        value = value,
        onValueChange = { },
        singleLine = true,
        enabled = false,
        label = { Text(text = label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier
            .clickable { clockState.show() }
            .fillMaxWidth()
    )
}

/**
 * Reusable outlined text field that displays a [DurationDialog] when clicked.
 * Users can select a duration following a minute/hour format.
 * Selections are converted to minutes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val durationState = rememberSheetState()
    val context = LocalContext.current

    DurationDialog(
        state = durationState, selection = DurationSelection { time ->
            val minutes = time / 60
            onValueChange("$minutes ${context.getString(R.string.mins)}")
        },
        config = DurationConfig(timeFormat = DurationFormat.HH_MM)
    )
    OutlinedTextField(
        value = value,
        onValueChange = { },
        singleLine = true,
        enabled = false,
        label = { Text(text = label) },
        colors = if (isError) {
            TextFieldDefaults.outlinedTextFieldColors(
                //Must do this because having enabled as false gets rid of isError color.
                disabledTextColor = MaterialTheme.colorScheme.error,
                disabledBorderColor = MaterialTheme.colorScheme.error,
                disabledPlaceholderColor = MaterialTheme.colorScheme.error,
                disabledLabelColor = MaterialTheme.colorScheme.error,
            )
        } else {
            TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        isError = isError,
        modifier = modifier
            .clickable { durationState.show() }
            .fillMaxWidth()
    )
}

/**
 * Reusable text field with parameters following the format of the trackers in this app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(onDone = { localFocusManager.clearFocus() }),
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
    )
}

/**
 * Reusable dropdown menu for selecting the "type" of an entry.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    list: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            singleLine = true,
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            isError = isError,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded }) {
            for (typeItem in list) {
                DropdownMenuItem(
                    text = {
                        Text(text = typeItem)
                    },
                    onClick = {
                        expanded =
                            !expanded; onValueChange(typeItem); focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

/**
 * Reusable button with parameters following the format of the trackers in this app.
 * Will indicate to users if a field was left empty on click.
 * If an entered data type has a date that does match the current date, a Toast will indicate the entry was saved in its according comprehensive log.
 */
@Composable
fun <T> EnterButton(
    detailsValue: T,
    entryDate: String,
    isError: Boolean,
    dataTypeName: String,
    onEnterClick: (T) -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row {
        Button(onClick = {
            if (isError) {
                Toast.makeText(context, R.string.a_required_field_is_empty, Toast.LENGTH_SHORT)
                    .show()
            } else if (entryDate != DateTimeFormatter.ofPattern("MM/dd/yy")
                    .format(LocalDate.now())
            ) {
                Toast.makeText(
                    context,
                    "$dataTypeName ${context.getString(R.string.stored_in_comprehensive)} $dataTypeName ${
                        context.getString(
                            R.string.log
                        )
                    }",
                    Toast.LENGTH_SHORT
                ).show()
                onEnterClick(detailsValue)

            } else {
                onEnterClick(detailsValue)
            }
        }) {
            Text(text = buttonText, textAlign = TextAlign.Center, modifier = modifier.width(100.dp))
        }
    }
}

/**
 * Daily bathroom log used in "BathroomTrackerBody".
 * The log filters by the selected dog's ID and [Bathroom.dogId], as well as by the current date and [Bathroom.date].
 * [LogColumnTitle] and [LogEntry] are from common.
 */
@Composable
fun DailyBathroomLog(
    bathroomList: List<Bathroom>,
    onDataTypeEntryClick: (Bathroom) -> Unit,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(175.dp)
    ) {
        Text(
            text = stringResource(R.string.bathroom_log),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.titleMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            LogColumnTitle(title = stringResource(id = R.string.date), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.time), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.time), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.notes), modifier.weight(.25F))
        }
        LazyColumn {
            items(
                items = bathroomList.filter {
                    it.dogId == selectedDogUiState.selectedDog.id && it.date == DateTimeFormatter.ofPattern(
                        "MM/dd/yy"
                    ).format(
                        LocalDate.now()
                    )
                },
                key = { bathroom -> bathroom.id }) { dataType ->    //I am not sure how to use generics with keys... having different logs for each tracker will have to stand, unfortunately...
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

/**
 * Daily food log used in "FoodTrackerBody".
 * The log filters by the selected dog's ID and [Food.dogId], as well as by the current date and [Food.date].
 * [LogColumnTitle] and [LogEntry] are from common.
 */
@Composable
fun DailyFoodLog(
    foodList: List<Food>,
    onDataTypeEntryClick: (Food) -> Unit,
    selectedDogUiState: SelectedDogUiState,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(175.dp)
    ) {
        Text(
            text = stringResource(R.string.daily_food_log),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.titleMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            LogColumnTitle(title = stringResource(id = R.string.date), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.calories), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.type), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.notes), modifier.weight(.25F))
        }
        LazyColumn {
            items(
                items = foodList.filter {
                    it.dogId == selectedDogUiState.selectedDog.id && it.date == DateTimeFormatter.ofPattern(
                        "MM/dd/yy"
                    ).format(
                        LocalDate.now()
                    )
                },
                key = { food -> food.id }) { dataType -> //I am not sure how to use generics with keys... having different logs for each tracker will have to stand, unfortunately...
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

/**
 * Daily bathroom log used in "WalkTrackerBody".
 * The log filters by the selected dog's ID and [Walk.dogId], as well as by the current date and [Walk.date].
 * [LogColumnTitle] and [LogEntry] are from common.
 */
@Composable
fun DailyWalkLog(
    walkList: List<Walk>,
    onDataTypeEntryClick: (Walk) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.height(175.dp)
    ) {
        Text(
            text = stringResource(R.string.daily_walk_log),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.titleMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            LogColumnTitle(title = stringResource(id = R.string.date), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(R.string.start), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(R.string.duration), modifier.weight(.25F))
            LogColumnTitle(title = stringResource(id = R.string.notes), modifier.weight(.25F))
        }
        LazyColumn {
            items(
                items = walkList,
                key = { walk -> walk.id }) { dataType -> //I am not sure how to use generics with keys... having different logs for each tracker will have to stand, unfortunately...
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

/**
 * Reusable composable for a tracker log's column title, both daily and comprehensive.
 */
@Composable
fun LogColumnTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .padding(2.dp)
    )
}

/**
 * Reusable composable for a tracker log's entry, both daily and comprehensive.
 */
@Composable
fun <T> LogEntry(
    dataType: T,
    entryValue1: T,
    entryValue2: T,
    entryValue3: T,
    entryValue4: T,
    onFoodEntryClick: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { deleteConfirmationRequired = true }
    ) {
        LogEntryValues(value = entryValue1, modifier = modifier.weight(.25F))
        LogEntryValues(value = entryValue2, modifier = modifier.weight(.25F))
        LogEntryValues(value = entryValue3, modifier = modifier.weight(.25F))
        LogEntryValues(value = entryValue4, modifier = modifier.weight(.25F))
        if (deleteConfirmationRequired) { //Taken from DogInformationScreen.kt
            DeleteConfirmationAlertDialog(onDeleteConfirm = {
                deleteConfirmationRequired = false
                onFoodEntryClick(dataType)
            }, onDeleteCancel = { deleteConfirmationRequired = false })
        }
    }
}

/**
 * Reusable composable for [LogEntry] entry values (determined by the type of tracker).
 */
@Composable
fun <T> LogEntryValues(value: T, modifier: Modifier = Modifier) {
    Text(
        text = value.toString(),
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(2.dp)
    )
}

/**
 * Delete confirmation dialog that appears when a user clicks on [LogEntry] in either a daily or comprehensive log.
 */
@Composable
fun DeleteConfirmationAlertDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(text = stringResource(id = R.string.attention)) },
        text = { Text(text = stringResource(id = R.string.are_you_sure_you_want)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(id = R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(id = R.string.yes))
            }
        }
    )
}

/**
 * Reusable text composable that navigates to its according comprehensive log on click.
 */
@Composable
fun ComprehensiveLogText(
    logName: String,
    onComprehensiveLogClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "${stringResource(id = R.string.comprehensive)} $logName ${stringResource(id = R.string.log)}",
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.clickable { onComprehensiveLogClick() })
}