package com.diettracker.core.snackbars

import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class SnackbarDispatcher {
    private val snackbarEmitter: EventEmitter<Snack> = EventEmitter()
    val snackbars: EventSource<Snack> = snackbarEmitter

    fun emitInfoSnackbar(msg: String, isLong: Boolean = false) {
        snackbarEmitter.emit(Snack(msg, SnackbarType.Info, isLong))
    }

    fun emitErrorSnackbar(msg: String, isLong: Boolean = false) {
        snackbarEmitter.emit(Snack(msg, SnackbarType.Error, isLong))
    }

    fun emitSuccessSnackbar(msg: String, isLong: Boolean = false) {
        snackbarEmitter.emit(Snack(msg, SnackbarType.Success, isLong))
    }
}