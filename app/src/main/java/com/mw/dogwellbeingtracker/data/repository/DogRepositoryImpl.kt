package com.mw.dogwellbeingtracker.data.repository

import com.mw.dogwellbeingtracker.data.data_source.DogDao
import com.mw.dogwellbeingtracker.domain.model.Dog
import com.mw.dogwellbeingtracker.domain.repository.DogRepository
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