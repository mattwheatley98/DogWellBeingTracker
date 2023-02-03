package com.mw.dogwellbeingtracker.data.data_source

import androidx.room.*
import com.mw.dogwellbeingtracker.domain.model.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * from food")
    fun getAllFoodStream(): Flow<List<Food>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT SUM(calories) from food")
    suspend fun calculateDailyCalories(): Int
}