package com.mw.dogwellbeingtracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "dogs")
data class Dog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val isSelected: Boolean = false,
    val name: String = "",
    val age: String = "",
    val breed: String = "",
    val weight: String = "",
    var dailyCurrentCalories: String = "0",
    val dailyMaxCalories: String = "0",
    val sex: String = "",
    val picture: String = "",
    var isEditFieldExpanded: Boolean = false,
    var storedDate: String = DateTimeFormatter.ofPattern("MM/dd/yy").format(LocalDate.now())
)