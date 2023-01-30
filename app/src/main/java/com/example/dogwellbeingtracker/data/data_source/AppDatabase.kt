package com.example.dogwellbeingtracker.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dogwellbeingtracker.domain.model.Bathroom
import com.example.dogwellbeingtracker.domain.model.Dog
import com.example.dogwellbeingtracker.domain.model.Food
import com.example.dogwellbeingtracker.domain.model.Walk

@Database(
    entities = [Bathroom::class, Dog::class, Food::class, Walk::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val bathroomDao: BathroomDao
    abstract val dogDao: DogDao
    abstract val foodDao: FoodDao
    abstract val walkDao: WalkDao

    companion object {
        const val DATABASE_NAME = "dog_wellbeing_tracker_database"
    }
}