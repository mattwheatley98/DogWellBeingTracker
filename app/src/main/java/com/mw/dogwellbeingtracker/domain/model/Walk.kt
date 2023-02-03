package com.mw.dogwellbeingtracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "walk")
data class Walk(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dogId: Int = 0,
    val date: String = "",
    val time: String = "",
    val duration: String = "",
    val notes: String = "",
    val timesWalked: String = ""
)