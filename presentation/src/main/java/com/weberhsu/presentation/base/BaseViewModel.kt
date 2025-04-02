package com.weberhsu.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel : ViewModel() {
    protected val _errorMsg = MutableSharedFlow<String>(replay = 0)
    val errorMsg: SharedFlow<String>
        get() = _errorMsg

    protected suspend fun handleErrorMsg(t: Throwable) {
        _errorMsg.emit(t.message.orEmpty())
    }
}