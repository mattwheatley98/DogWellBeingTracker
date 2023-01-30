package com.example.dogwellbeingtracker.presentation.bottom_bar_screens.food_tracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogwellbeingtracker.domain.model.Dog
import com.example.dogwellbeingtracker.domain.model.Food
import com.example.dogwellbeingtracker.domain.repository.DogRepository
import com.example.dogwellbeingtracker.domain.repository.FoodRepository
import com.example.dogwellbeingtracker.presentation.common.selectedDogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * View Model for [FoodTrackerLogScreen] and [FoodTrackerScreen].
 * Also used in "HomeScreen" to access [foodListUiState] for the daily summary.
 * Needs access to [dogRepository] in addition to its own repository, unlike the other View Models.
 */
@HiltViewModel
class FoodTrackerViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val dogRepository: DogRepository
) : ViewModel() {
    /**
     * Holds [FoodListUiState].
     * The data is retrieved from [FoodRepository] and mapped to the UI state.
     */
    val foodListUiState: StateFlow<FoodListUiState> =
        foodRepository.getAllFoodStream()
            .map {
                FoodListUiState(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FoodListUiState()
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Holds [FoodTrackerUiState] for the input fields from [EnterFood].
     */
    var foodTrackerUiState by mutableStateOf(FoodTrackerUiState())

    /**
     * Updates [foodTrackerUiState] with values provided by the input fields from [EnterFood]
     */
    fun updateUiState(foodDetails: FoodDetails) {
        foodTrackerUiState = FoodTrackerUiState(foodDetails = foodDetails)
    }

    /**
     * Inserts a [Food] entry in the Room database and resets [FoodDetails].
     * Also updates [Dog.dailyCurrentCalories] for the selected dog if [Food.date] is the present date.
     */
    suspend fun addFood(food: Food) {
        foodRepository.insertFood(foodTrackerUiState.foodDetails.toFood())
        foodTrackerUiState = FoodTrackerUiState(
            foodDetails = FoodDetails(
                id = 0,
                dogId = 0,
                date = DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()),
                type = "",
                calories = "",
                notes = ""
            )
        )
        val dailyCurrentCalories =
            selectedDogUiState.value.selectedDog.dailyCurrentCalories.toIntOrNull() ?: 0
        val enteredCalories = food.calories.toIntOrNull() ?: 0
        val runningCalorieCount = dailyCurrentCalories + enteredCalories
        if (food.date == DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now())) {
            selectedDogUiState.value.selectedDog.dailyCurrentCalories =
                runningCalorieCount.toString()
        }
        dogRepository.updateDog(
            Dog(
                id = selectedDogUiState.value.selectedDog.id,
                isSelected = true,
                name = selectedDogUiState.value.selectedDog.name,
                age = selectedDogUiState.value.selectedDog.age,
                breed = selectedDogUiState.value.selectedDog.breed,
                weight = selectedDogUiState.value.selectedDog.weight,
                dailyCurrentCalories = runningCalorieCount.toString(),
                dailyMaxCalories = selectedDogUiState.value.selectedDog.dailyMaxCalories,
                sex = selectedDogUiState.value.selectedDog.sex,
                picture = selectedDogUiState.value.selectedDog.picture,
                isEditFieldExpanded = selectedDogUiState.value.selectedDog.isEditFieldExpanded,
                storedDate = selectedDogUiState.value.selectedDog.storedDate
            )
        )
    }

    /**
     * Deletes a [Food] entry in the Room database and updates [Dog.dailyCurrentCalories] for the selected dog.
     */
    suspend fun deleteFood(food: Food) {
        foodRepository.deleteFood(food)
        val dailyCurrentCalories =
            selectedDogUiState.value.selectedDog.dailyCurrentCalories.toIntOrNull() ?: 0
        val enteredCalories = food.calories.toIntOrNull() ?: 0
        val runningCalorieCount = if (dailyCurrentCalories - enteredCalories < 0) { 0 } else { dailyCurrentCalories - enteredCalories }

        selectedDogUiState.value.selectedDog.dailyCurrentCalories = runningCalorieCount.toString()
        dogRepository.updateDog(
            Dog(
                id = selectedDogUiState.value.selectedDog.id,
                isSelected = true,
                name = selectedDogUiState.value.selectedDog.name,
                age = selectedDogUiState.value.selectedDog.age,
                breed = selectedDogUiState.value.selectedDog.breed,
                weight = selectedDogUiState.value.selectedDog.weight,
                dailyCurrentCalories = runningCalorieCount.toString(),
                dailyMaxCalories = selectedDogUiState.value.selectedDog.dailyMaxCalories,
                sex = selectedDogUiState.value.selectedDog.sex,
                picture = selectedDogUiState.value.selectedDog.picture,
                isEditFieldExpanded = selectedDogUiState.value.selectedDog.isEditFieldExpanded,
                storedDate = selectedDogUiState.value.selectedDog.storedDate
            )
        )
    }
}

/**
 * Represents UI state for [Food].
 */
data class FoodTrackerUiState(
    val foodDetails: FoodDetails = FoodDetails(),
)

/**
 * UI state for the input fields from [EnterFood].
 */
data class FoodDetails(
    val id: Int = 0,
    val dogId: Int = 0,
    val date: String = DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now()),
    val type: String = "",
    val calories: String = "",
    val notes: String = "",
)

/**
 * Extension function to convert [FoodTrackerUiState] to [Food]
 */
fun FoodDetails.toFood(): Food = Food(
    id = id,
    dogId = dogId,
    date = date,
    type = type,
    calories = calories,
    notes = notes
)

/**
 * UI state for the food list.
 */
data class FoodListUiState(val foodList: List<Food> = listOf())