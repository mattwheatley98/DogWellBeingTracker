package com.example.dogwellbeingtracker.di

import android.app.Application
import androidx.room.Room
import com.example.dogwellbeingtracker.data.data_source.AppDatabase
import com.example.dogwellbeingtracker.data.repository.BathroomRepositoryImpl
import com.example.dogwellbeingtracker.data.repository.DogRepositoryImpl
import com.example.dogwellbeingtracker.data.repository.FoodRepositoryImpl
import com.example.dogwellbeingtracker.data.repository.WalkRepositoryImpl
import com.example.dogwellbeingtracker.domain.repository.BathroomRepository
import com.example.dogwellbeingtracker.domain.repository.DogRepository
import com.example.dogwellbeingtracker.domain.repository.FoodRepository
import com.example.dogwellbeingtracker.domain.repository.WalkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBathroomRepository(database: AppDatabase): BathroomRepository {
        return BathroomRepositoryImpl(database.bathroomDao)
    }

    @Provides
    @Singleton
    fun provideDogRepository(database: AppDatabase): DogRepository {
        return DogRepositoryImpl(database.dogDao)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(database: AppDatabase): FoodRepository {
        return FoodRepositoryImpl(database.foodDao)
    }

    @Provides
    @Singleton
    fun provideWalkRepository(database: AppDatabase): WalkRepository {
        return WalkRepositoryImpl(database.walkDao)
    }
}