package com.mw.dogwellbeingtracker.domain.repository

import com.mw.dogwellbeingtracker.domain.model.Walk
import kotlinx.coroutines.flow.Flow

interface WalkRepository {
    fun getAllWalksStream(): Flow<List<Walk>>

    suspend fun insertWalk(walk: Walk)

    suspend fun deleteWalk(walk: Walk)
}