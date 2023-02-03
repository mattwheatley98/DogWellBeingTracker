package com.mw.dogwellbeingtracker.domain.repository

import com.mw.dogwellbeingtracker.domain.model.Bathroom
import kotlinx.coroutines.flow.Flow

interface BathroomRepository {
    fun getAllBathroomsStream(): Flow<List<Bathroom>>

    suspend fun insertBathroom(bathroom: Bathroom)

    suspend fun deleteBathroom(bathroom: Bathroom)
}