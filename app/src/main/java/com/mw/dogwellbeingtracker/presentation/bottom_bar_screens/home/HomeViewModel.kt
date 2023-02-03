package com.mw.dogwellbeingtracker.presentation.bottom_bar_screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mw.dogwellbeingtracker.domain.model.Dog
import com.mw.dogwellbeingtracker.domain.repository.DogRepository
import com.mw.dogwellbeingtracker.presentation.common._selectedDogUiState
import com.mw.dogwellbeingtracker.presentation.common.selectedDogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Interface that contains the status of the dog list's initialization.
 */
interface DogUiState {
    object Success : DogUiState
    object NoDogs : DogUiState
    object Loading : DogUiState
}

/**
 * View Model for [HomeScreen].
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val dogRepository: DogRepository) : ViewModel() {
    /**
     * Holds [DogUiState].
     */
    var dogUiState: DogUiState by mutableStateOf(DogUiState.Loading)
        private set

    /**
     * Holds [DogListUiState].
     * The data is retrieved from [DogRepository] and mapped to the UI state.
     */
    val dogListUiState: StateFlow<DogListUiState> =
        dogRepository.getAllDogsStream().map { DogListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DogListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Updates [_selectedDogUiState] to take on [selectedDog].
     * Also resets all [Dog.isSelected] to false, then sets the newly selected dog to true.
     */
    suspend fun selectDog(selectedDog: Dog) {
        dogRepository.resetSelectedDog()
        dogRepository.updateDog(selectedDog.copy(isSelected = true))
        _selectedDogUiState.update { it.copy(selectedDog = selectedDog) }
    }

    /**
     * Sets [DogUiState.Loading] and gets the dog with [Dog.isSelected].
     * If [DogUiState.Loading] does not change after a delay, it changes to [DogUiState.NoDogs]
     */
    suspend fun getSelectedDog() {
        dogUiState = DogUiState.Loading
        delay(100)
        for (dog in dogListUiState.value.dogList) {
            if (dog.isSelected) {
                selectDog(dog)
                dogUiState = DogUiState.Success
            }
        }
        delay(1000)
        if (dogUiState == DogUiState.Loading) {
            dogUiState = DogUiState.NoDogs
        }
    }

    /**
     * Checks the current date compared to [Dog.storedDate].
     * If the dates do not match, this indicates a day has passed, so [Dog.dailyCurrentCalories] is reset to 0 and [Dog.storedDate] is updated.
     */
    suspend fun dateChangeCheck() {
        if (selectedDogUiState.value.selectedDog.storedDate != DateTimeFormatter.ofPattern("MM/dd/yy")
                .format(LocalDate.now())
        ) {
            selectedDogUiState.value.selectedDog.dailyCurrentCalories = "0"
            dogRepository.updateDog(
                Dog(
                    id = selectedDogUiState.value.selectedDog.id,
                    isSelected = true,
                    name = selectedDogUiState.value.selectedDog.name,
                    age = selectedDogUiState.value.selectedDog.age,
                    breed = selectedDogUiState.value.selectedDog.breed,
                    weight = selectedDogUiState.value.selectedDog.weight,
                    dailyCurrentCalories = "0",
                    dailyMaxCalories = selectedDogUiState.value.selectedDog.dailyMaxCalories,
                    sex = selectedDogUiState.value.selectedDog.sex,
                    picture = selectedDogUiState.value.selectedDog.picture,
                    isEditFieldExpanded = selectedDogUiState.value.selectedDog.isEditFieldExpanded,
                    storedDate = DateTimeFormatter.ofPattern("MM/dd/yy")
                        .format(
                            LocalDate.now()
                        )
                )
            )
        }
    }

    init {
        viewModelScope.launch {
            delay(100)
            getSelectedDog()
            dateChangeCheck()
        }
    }
}

/**
 * UI state for the dog list.
 */
data class DogListUiState(
    val dogList: List<Dog> = listOf(),
)


