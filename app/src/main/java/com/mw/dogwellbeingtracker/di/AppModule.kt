package com.mw.dogwellbeingtracker.di

import android.app.Application
import androidx.room.Room
import com.mw.dogwellbeingtracker.data.data_source.AppDatabase
import com.mw.dogwellbeingtracker.data.repository.BathroomRepositoryImpl
import com.mw.dogwellbeingtracker.data.repository.DogRepositoryImpl
import com.mw.dogwellbeingtracker.data.repository.FoodRepositoryImpl
import com.mw.dogwellbeingtracker.data.repository.WalkRepositoryImpl
import com.mw.dogwellbeingtracker.domain.repository.BathroomRepository
import com.mw.dogwellbeingtracker.domain.repository.DogRepository
import com.mw.dogwellbeingtracker.domain.repository.FoodRepository
import com.mw.dogwellbeingtracker.domain.repository.WalkRepository
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