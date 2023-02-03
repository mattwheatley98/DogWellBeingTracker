package com.mw.dogwellbeingtracker.data.repository

import com.mw.dogwellbeingtracker.data.data_source.WalkDao
import com.mw.dogwellbeingtracker.domain.model.Walk
import com.mw.dogwellbeingtracker.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow

class WalkRepositoryImpl(private val walkDao: WalkDao) : WalkRepository {
    override fun getAllWalksStream(): Flow<List<Walk>> = walkDao.getAllWalksStream()

    override suspend fun insertWalk(walk: Walk) = walkDao.insertWalk(walk)

    override suspend fun deleteWalk(walk: Walk) = walkDao.deleteWalk(walk)
}