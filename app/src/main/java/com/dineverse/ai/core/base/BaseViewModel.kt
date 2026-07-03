package com.dineverse.ai.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel : ViewModel() {
    protected val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    protected suspend fun handleError(e: Throwable) {
        val message = e.message ?: "An unknown error occurred"
        _errorFlow.emit(message)
    }
}
