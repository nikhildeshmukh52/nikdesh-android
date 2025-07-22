package com.android.androidinternals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RandomNumberViewModel: ViewModel() {

    private val _randomNumber = MutableStateFlow(0)
    val randomNumber = _randomNumber.asStateFlow()

    private val _boundService = MutableStateFlow(false)
    val boundService = _boundService.asStateFlow()

    fun generateRandomNumber(service: RandomNumberService) {
        viewModelScope.launch {
            val deferred = service.generateRandomNumber()
            val value = deferred.await()
            _randomNumber.value = value
        }
    }

    fun serviceConnected() {
        _boundService.value = true
    }

    fun serviceDisconnected() {
        _boundService.value = false
    }
}