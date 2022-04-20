package com.diettracker.core.snackbars

sealed class SnackbarType{
    object Info: SnackbarType()
    object Success: SnackbarType()
    object Error: SnackbarType()
}
