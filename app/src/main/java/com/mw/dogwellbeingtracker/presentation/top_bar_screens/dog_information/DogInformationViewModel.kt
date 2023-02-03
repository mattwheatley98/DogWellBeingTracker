package com.mw.dogwellbeingtracker.presentation.top_bar_screens.dog_information

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
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * View Model for [DogInformationScreen].
 */
@HiltViewModel
class DogInformationViewModel @Inject constructor(private val dogRepository: DogRepository) : ViewModel() {
    /**
     * Holds [DogInformationUiState].
     * The data is retrieved from [DogRepository] and mapped to the UI state.
     */
    val dogInformationUiState: StateFlow<DogInformationUiState> =
        dogRepository.getAllDogsStream().map { DogInformationUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DogInformationUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Holds [EditDogUiState] from the input fields from [EditDogTextFields] and [EditDogButtons] (if a picture was changed).
     */
    var editDogUiState by mutableStateOf(EditDogUiState())
        private set

    /**
     * Updates [editDogUiState] with values provided by the input fields from [EditDogTextFields] and [EditDogButtons] (if a picture was changed).
     */
    fun updateEditDogUiState(editDogDetails: EditDogDetails) {
        editDogUiState = EditDogUiState(editDogDetails = editDogDetails)
    }

    /**
     * Converts [Dog] to [EditDogDetails] to allow for editing with their designated input field.
     */
    fun convertDogToEditDogDetails(dog: Dog) {
        editDogUiState = EditDogUiState(
            editDogDetails = EditDogDetails(
                id = dog.id,
                name = dog.name,
                age = dog.age,
                breed = dog.breed,
                weight = dog.weight,
                dailyMaxCalories = dog.dailyMaxCalories,
                sex = dog.sex,
                picture = dog.picture
            )
        )
    }

    /**
     * Deletes a [Dog] entry in the Room database.
     */
    suspend fun deleteDog(dog: Dog) {
        dogRepository.deleteDog(dog)
    }

    /**
     * Updates a dog entry in the Room database with [EditDogDetails].
     */
    suspend fun updateDog() {
        dogRepository.updateDog(editDogUiState.editDogDetails.toDog())
    }

    /**
     * Sets the clicked on entry to [Dog.isEditFieldExpanded] to expand their edit field.
     */
    suspend fun expandEditTextField(dog: Dog) {
        dogRepository.updateDog(dog.copy(isEditFieldExpanded = true))
    }

    /**
     * Sets all [Dog.isEditFieldExpanded] to false.
     */
    suspend fun resetEditFieldExpansion() {
        dogRepository.resetEditFieldExpansion()
    }

    /**
     * Updates the selected dog.
     */
    suspend fun selectDog(selectedDog: Dog) {
        dogRepository.resetSelectedDog()
        dogRepository.updateDog(selectedDog.copy(isSelected = true))
        _selectedDogUiState.update { it.copy(selectedDog = selectedDog) }
    }
}

/**
 * Represents UI state for [EditDogUiState]
 */
data class EditDogUiState(
    val editDogDetails: EditDogDetails = EditDogDetails()
)

/**
 * UI state for the input fields from [EditDogTextFields] and [EditDogButtons].
 */
data class EditDogDetails(
    val id: Int = 0,
    var name: String = "",
    var age: String = "",
    var breed: String = "",
    var weight: String = "",
    var dailyCurrentCalories: String = selectedDogUiState.value.selectedDog.dailyCurrentCalories,
    var dailyMaxCalories: String = "",
    var sex: String = "",
    var picture: String = "",
    var selected: Boolean = false,
)

/**
 * Extension function to convert [EditDogUiState] to [Dog].
 */
fun EditDogDetails.toDog(): Dog = Dog(
    id = id,
    name = name,
    age = age,
    breed = breed,
    weight = weight,
    dailyCurrentCalories = dailyCurrentCalories,
    dailyMaxCalories = dailyMaxCalories,
    sex = sex,
    picture = picture,
    isEditFieldExpanded = selected,
)

/**
 * UI state for the dog list.
 */
data class DogInformationUiState(
    val dogList: List<Dog> = listOf(),
)