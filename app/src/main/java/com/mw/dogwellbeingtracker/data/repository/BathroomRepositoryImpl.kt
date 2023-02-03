package com.mw.dogwellbeingtracker.data.repository

import com.mw.dogwellbeingtracker.data.data_source.BathroomDao
import com.mw.dogwellbeingtracker.domain.model.Bathroom
import com.mw.dogwellbeingtracker.domain.repository.BathroomRepository
import kotlinx.coroutines.flow.Flow

class BathroomRepositoryImpl(private val bathroomDao: BathroomDao) : BathroomRepository {
    override fun getAllBathroomsStream(): Flow<List<Bathroom>> = bathroomDao.getAllBathroomsStream()

    override suspend fun insertBathroom(bathroom: Bathroom) = bathroomDao.insertBathroom(bathroom)

    override suspend fun deleteBathroom(bathroom: Bathroom) = bathroomDao.deleteBathroom(bathroom)
}