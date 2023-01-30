package com.example.dogwellbeingtracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bathroom")
data class Bathroom(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dogId: Int = 0,
    val date: String = "",
    val time: String = "",
    val type: String = "",
    val notes: String = "",
    val timesPeed: String = "",
    val timesPooped: String = "",
)