package com.cadrikmdev.bluetoothintercom.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

open class BaseViewModel: ViewModel() {

    protected val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading
        .onStart { loadInitialData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    open fun loadInitialData() {}
}