package com.android.androidinternals

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PackageInfoViewModel: ViewModel() {
    private val _packageInfo = MutableStateFlow(PackageData())
    val packageInfo = _packageInfo.asStateFlow()

    private val _boundService = MutableStateFlow(false)
    val boundService = _boundService.asStateFlow()

    fun serviceConnected() {
        _boundService.value = true
    }

    fun serviceDisconnected() {
        _boundService.value = false
    }

    fun updatePackageInfo(packageData: PackageData) {
        _packageInfo.value = packageData
    }
}