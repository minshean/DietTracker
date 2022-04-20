package com.diettracker.core.snackbars

data class Snack(
    val msg: String,
    val snackbarType: SnackbarType = SnackbarType.Info,
    val isLong: Boolean = false
)
