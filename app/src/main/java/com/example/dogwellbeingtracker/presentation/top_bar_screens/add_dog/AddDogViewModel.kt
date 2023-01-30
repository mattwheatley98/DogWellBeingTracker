package com.example.dogwellbeingtracker.presentation.top_bar_screens.add_dog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dogwellbeingtracker.domain.model.Dog
import com.example.dogwellbeingtracker.domain.repository.DogRepository
import com.example.dogwellbeingtracker.presentation.common._selectedDogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * View Model for [AddDogScreen].
 */
@HiltViewModel
class AddDogViewModel @Inject constructor(private val dogRepository: DogRepository) : ViewModel() {
    /**
     * Holds [AddDogUiState] from the input fields from [AddDogTextFields] and [UploadedDogPicture].
     */
    var addDogUiState by mutableStateOf(AddDogUiState())
        private set

    /**
     * Updates [addDogUiState] with values provided by the input fields from [AddDogTextFields] and [UploadedDogPicture].
     */
    fun updateUiState(dogDetails: DogDetails) {
        addDogUiState = AddDogUiState(dogDetails = dogDetails)
    }

    /**
     * Inserts a [Dog] entry to the Room database and resets [DogDetails] and edit fields.
     */
    suspend fun addDog() {
        dogRepository.resetSelectedDog()
        dogRepository.insertDog(addDogUiState.dogDetails.toDog())
        _selectedDogUiState.update { it.copy(selectedDog = addDogUiState.dogDetails.toDog()) }
        addDogUiState = AddDogUiState(
            dogDetails = DogDetails(
                id = 0,
                name = "",
                age = "",
                breed = "",
                weight = "",
                dailyMaxCalories = "",
                sex = "",
                picture = ""
            )
        )
        dogRepository.resetEditFieldExpansion()
    }
}

/**
 * Represents UI state for [Dog]
 */
data class AddDogUiState(
    val dogDetails: DogDetails = DogDetails()
)

/**
 * Ui state for the input fields from [AddDogTextFields] and [UploadedDogPicture].
 */
data class DogDetails(
    val id: Int = 0,
    var name: String = "",
    val isSelected: Boolean = true,
    var age: String = "",
    var breed: String = "",
    var weight: String = "",
    var dailyMaxCalories: String = "",
    var sex: String = "",
    var picture: String = "",
)

/**
 * Extension function to convert [AddDogUiState] to [Dog].
 */
private fun DogDetails.toDog(): Dog = Dog(
    id = id,
    name = name,
    isSelected = isSelected,
    age = age,
    breed = breed,
    weight = weight,
    dailyMaxCalories = dailyMaxCalories,
    sex = sex,
    picture = picture,
)