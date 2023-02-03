package com.mw.dogwellbeingtracker.data.data_source

import androidx.room.*
import com.mw.dogwellbeingtracker.domain.model.Bathroom
import kotlinx.coroutines.flow.Flow

@Dao
interface BathroomDao {
    @Query("SELECT * from bathroom")
    fun getAllBathroomsStream(): Flow<List<Bathroom>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBathroom(bathroom: Bathroom)

    @Delete
    suspend fun deleteBathroom(bathroom: Bathroom)
}