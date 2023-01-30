package com.example.dogwellbeingtracker.domain.repository

import com.example.dogwellbeingtracker.domain.model.Bathroom
import kotlinx.coroutines.flow.Flow

interface BathroomRepository {
    fun getAllBathroomsStream(): Flow<List<Bathroom>>

    suspend fun insertBathroom(bathroom: Bathroom)

    suspend fun deleteBathroom(bathroom: Bathroom)
}