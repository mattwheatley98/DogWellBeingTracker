package com.mw.dogwellbeingtracker.presentation.bottom_bar_screens.bathroom_tracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mw.dogwellbeingtracker.domain.model.Bathroom
import com.mw.dogwellbeingtracker.domain.repository.BathroomRepository
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
 * View Model for [BathroomTrackerLogScreen] and [BathroomTrackerScreen].
 * Also used in "HomeScreen" to access [bathroomListUiState] for the daily summary.
 */
@HiltViewModel
class BathroomTrackerViewModel @Inject constructor(private val bathroomRepository: BathroomRepository) :
    ViewModel() {
    /**
     * Holds [BathroomListUiState].
     * The data is retrieved from [BathroomRepository] and mapped to the UI state.
     */
    val bathroomListUiState: StateFlow<BathroomListUiState> =
        bathroomRepository.getAllBathroomsStream()
            .map { BathroomListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BathroomListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Holds [BathroomTrackerUiState] for the input fields from [EnterBathroom].
     */
    var bathroomTrackerUiState by mutableStateOf(BathroomTrackerUiState())
        private set

    /**
     * Updates [bathroomTrackerUiState] with values provided by the input fields from [EnterBathroom].
     */
    fun updateUiState(bathroomDetails: BathroomDetails) {
        bathroomTrackerUiState = BathroomTrackerUiState(bathroomDetails = bathroomDetails)
    }

    /**
     * Inserts a [Bathroom] entry to the Room database and resets [BathroomDetails].
     */
    suspend fun addBathroom() {
        bathroomRepository.insertBathroom(bathroomTrackerUiState.bathroomDetails.toBathroom())
        bathroomTrackerUiState = BathroomTrackerUiState(
            bathroomDetails = BathroomDetails(
                id = 0,
                dogId = 0,
                date = DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()),
                time = DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now()),
                type = "",
                notes = ""
            )
        )
    }

    /**
     * Deletes a [Bathroom] entry in the Room database.
     */
    suspend fun deleteBathroom(bathroom: Bathroom) {
        bathroomRepository.deleteBathroom(bathroom)
    }
}

/**
 * Represents UI state for [Bathroom].
 */
data class BathroomTrackerUiState(
    val bathroomDetails: BathroomDetails = BathroomDetails()
)

/**
 * UI state for the input fields from [EnterBathroom].
 */
data class BathroomDetails(
    val id: Int = 0,
    val dogId: Int = 0,
    val date: String = DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()),
    val time: String = DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now()),
    val type: String = "",
    val notes: String = "",
)

/**
 * Extension function to convert [BathroomTrackerUiState] to [Bathroom].
 */
private fun BathroomDetails.toBathroom(): Bathroom = Bathroom(
    id = id,
    dogId = dogId,
    date = date,
    time = time,
    type = type,
    notes = notes
)

/**
 * UI state for the bathroom list.
 */
data class BathroomListUiState(val bathroomList: List<Bathroom> = listOf())