package com.android.androidinternals

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel: ViewModel() {

    private val _serviceConnected = MutableStateFlow(false)
    val serviceConnected = _serviceConnected.asStateFlow()

    private val _productInfo = MutableStateFlow(Product())
    val productInfo = _productInfo.asStateFlow()

    fun connectService() {
        _serviceConnected.value = true
    }

    fun disconnectService() {
        _serviceConnected.value = false
    }

    fun updateProductInfo(service: IProduct) {
        _productInfo.value = service.product
    }
}