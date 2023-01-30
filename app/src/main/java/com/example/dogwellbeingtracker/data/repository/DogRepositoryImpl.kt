package com.example.dogwellbeingtracker.data.repository

import com.example.dogwellbeingtracker.data.data_source.DogDao
import com.example.dogwellbeingtracker.domain.model.Dog
import com.example.dogwellbeingtracker.domain.repository.DogRepository
import kotlinx.coroutines.flow.Flow

class DogRepositoryImpl(private val dogDao: DogDao) : DogRepository {
    override fun getAllDogsStream(): Flow<List<Dog>> = dogDao.getAllDogsStream()

    override suspend fun insertDog(dog: Dog) = dogDao.insertDog(dog)

    override suspend fun updateDog(dog: Dog) = dogDao.updateDog(dog)

    override suspend fun deleteDog(dog: Dog) = dogDao.deleteDog(dog)

    override suspend fun resetSelectedDog() = dogDao.resetSelectedDog()

    override suspend fun getSelectedDog(): Dog = dogDao.getSelectedDog()

    override suspend fun resetEditFieldExpansion() = dogDao.resetEditFieldExpansion()

}