package com.example.dogwellbeingtracker.data.data_source

import androidx.room.*
import com.example.dogwellbeingtracker.domain.model.Walk
import kotlinx.coroutines.flow.Flow

@Dao
interface WalkDao {
    @Query("SELECT * from walk")
    fun getAllWalksStream(): Flow<List<Walk>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWalk(walk: Walk)

    @Delete
    suspend fun deleteWalk(walk: Walk)
}