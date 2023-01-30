package com.example.dogwellbeingtracker.domain.repository

import com.example.dogwellbeingtracker.domain.model.Walk
import kotlinx.coroutines.flow.Flow

interface WalkRepository {
    fun getAllWalksStream(): Flow<List<Walk>>

    suspend fun insertWalk(walk: Walk)

    suspend fun deleteWalk(walk: Walk)
}