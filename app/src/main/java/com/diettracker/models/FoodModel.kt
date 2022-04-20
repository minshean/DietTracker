package com.diettracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class FoodModel(
    val name: String,
    val calories: Int,
    val time: Long,
    val type: String,

): Parcelable {
    constructor(): this("", 0, 0L, "")

    fun isSameDate(date: Date): Boolean{
        val currentDate = Date(this.time)
        return currentDate.date == date.date && currentDate.month == date.month && currentDate.year == date.year
    }
}

@Parcelize
data class ExerciseModel(
    val name: String,
    val time: Long,
    val calories: Double,
): Parcelable {
    constructor(): this("", 0L, 0.0)

    fun isSameDate(date: Date): Boolean{
        val currentDate = Date(this.time)
        return currentDate.date == date.date && currentDate.month == date.month && currentDate.year == date.year
    }
}
