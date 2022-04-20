package com.diettracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toddMMyyyy(): String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.tohhmmss(): String{
    val dateFormat = SimpleDateFormat("hh:mm a")
    return dateFormat.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toDateAndTime(): String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")
    return dateFormat.format(this)
}