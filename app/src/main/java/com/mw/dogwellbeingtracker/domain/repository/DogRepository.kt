package com.mw.dogwellbeingtracker.domain.repository

import com.mw.dogwellbeingtracker.domain.model.Dog
import kotlinx.coroutines.flow.Flow

interface DogRepository {
    fun getAllDogsStream(): Flow<List<Dog>>

    suspend fun insertDog(dog: Dog)

    suspend fun updateDog(dog: Dog)

    suspend fun deleteDog(dog: Dog)

    suspend fun resetSelectedDog()

    suspend fun getSelectedDog(): Dog

    suspend fun resetEditFieldExpansion()
}