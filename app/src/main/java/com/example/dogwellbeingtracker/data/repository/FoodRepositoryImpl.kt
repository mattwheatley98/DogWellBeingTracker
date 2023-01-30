package com.example.dogwellbeingtracker.data.repository

import com.example.dogwellbeingtracker.data.data_source.FoodDao
import com.example.dogwellbeingtracker.domain.model.Food
import com.example.dogwellbeingtracker.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow

class FoodRepositoryImpl(private val foodDao: FoodDao) : FoodRepository {
    override fun getAllFoodStream(): Flow<List<Food>> = foodDao.getAllFoodStream()

    override suspend fun insertFood(food: Food) = foodDao.insertFood(food)

    override suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)

    override suspend fun calculateDailyCalories(): Int = foodDao.calculateDailyCalories()

}