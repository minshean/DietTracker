package com.diettracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemModel(
    val id: String,
    val name: String,
    val calories: Int,
): Parcelable {
    constructor(): this("", "", 0)
}
