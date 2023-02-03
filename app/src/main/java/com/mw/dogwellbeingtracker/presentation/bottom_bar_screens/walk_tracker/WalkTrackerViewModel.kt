package com.mw.dogwellbeingtracker.presentation.bottom_bar_screens.walk_tracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mw.dogwellbeingtracker.domain.model.Walk
import com.mw.dogwellbeingtracker.domain.repository.WalkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * View Model for [WalkTrackerLogScreen] and [WalkTrackerScreen].
 * Also used in "HomeScreen" to access [walkListUiState] for the daily summary.
 */
@HiltViewModel
class WalkTrackerViewModel @Inject constructor(private val walkRepository: WalkRepository) :
    ViewModel() {
    /**
     * Holds [WalkListUiState].
     * The data is retrieved from [WalkRepository] and mapped to the UI state.
     */
    var walkListUiState: StateFlow<WalkListUiState> =
        walkRepository.getAllWalksStream()
            .map {
                WalkListUiState(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WalkListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Holds [WalkTrackerUiState] for the input fields from [EnterWalk].
     */
    var walkTrackerUiState by mutableStateOf(WalkTrackerUiState())
        private set

    /**
     * Updates [walkTrackerUiState] with values provided by the input fields from [EnterWalk].
     */
    fun updateUiState(walkDetails: WalkDetails) {
        walkTrackerUiState = WalkTrackerUiState(walkDetails = walkDetails)
    }

    /**
     * Inserts a [Walk] entry to the Room database and resets [WalkDetails].
     */
    suspend fun addWalk() {
        walkRepository.insertWalk(walkTrackerUiState.walkDetails.toWalk())
        walkTrackerUiState = WalkTrackerUiState(
            walkDetails = WalkDetails(
                id = 0,
                dogId = 0,
                date = DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()),
                time = DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now()),
                duration = "",
                notes = "",
                timesWalked = ""
            )
        )
    }

    /**
     * Deletes a [Walk] entry in the Room database.
     */
    suspend fun deleteWalk(walk: Walk) {
        walkRepository.deleteWalk(walk)
    }
}

/**
 * Represents UI state for [Walk].
 */
data class WalkTrackerUiState(
    val walkDetails: WalkDetails = WalkDetails()
)

/**
 * UI state for the input fields from [EnterWalk].
 */
data class WalkDetails(
    val id: Int = 0,
    val dogId: Int = 0,
    val date: String = DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()),
    val time: String = DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now()),
    val duration: String = "",
    val notes: String = "",
    val timesWalked: String = ""

)

/**
 * Extension function to convert [WalkTrackerUiState] to [Walk]
 */
private fun WalkDetails.toWalk(): Walk = Walk(
    id = id,
    dogId = dogId,
    date = date,
    time = time,
    duration = duration,
    notes = notes,
    timesWalked = timesWalked
)

/**
 * UI state for the walk list.
 */
data class WalkListUiState(val walkList: List<Walk> = listOf())