package com.android.androidinternals

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

class RandomNumberService: Service() {

    private val binder = LocalBinder()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var randomNumber = 0

    inner class LocalBinder: Binder() {
        fun getService() = this@RandomNumberService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun generateRandomNumber(): Deferred<Int> = coroutineScope.async {
        randomNumber = (0..100).random()
        randomNumber
    }
}