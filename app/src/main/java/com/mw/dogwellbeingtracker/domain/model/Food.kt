package com.mw.dogwellbeingtracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dogId: Int = 0,
    var date: String,
    var notes: String,
    var calories: String,
    var type: String
)