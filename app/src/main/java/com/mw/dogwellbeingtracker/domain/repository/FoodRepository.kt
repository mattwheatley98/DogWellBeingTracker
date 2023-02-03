package com.mw.dogwellbeingtracker.domain.repository

import com.mw.dogwellbeingtracker.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getAllFoodStream(): Flow<List<Food>>

    suspend fun insertFood(food: Food)

    suspend fun deleteFood(food: Food)

    suspend fun calculateDailyCalories(): Int
}