package com.example.dogwellbeingtracker.data.data_source

import androidx.room.*
import com.example.dogwellbeingtracker.domain.model.Dog
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {
    @Query("SELECT * from dogs ORDER BY name ASC")
    fun getAllDogsStream(): Flow<List<Dog>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDog(dog: Dog)

    @Update
    suspend fun updateDog(dog: Dog)

    @Delete
    suspend fun deleteDog(dog: Dog)

    @Query("UPDATE dogs SET isSelected = 0 WHERE isSelected = 1")
    suspend fun resetSelectedDog()

    @Query("SELECT * from dogs WHERE isSelected = 1")
    suspend fun getSelectedDog(): Dog

    @Query("UPDATE dogs SET isEditFieldExpanded = 0 WHERE isEditFieldExpanded = 1")
    suspend fun resetEditFieldExpansion()

}