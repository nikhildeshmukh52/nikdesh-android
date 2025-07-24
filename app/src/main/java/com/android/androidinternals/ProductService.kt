package com.android.androidinternals

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ProductService: Service() {

    private val lock = Any()

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    private fun getProduct(): Product = synchronized(lock) {
        return Product().apply {
            packageName = "com.android.androidinternals" + "${(0..100).random()}"
            pid = (0..10).random()
            user = "${(0..100).random()}"
        }
    }

    private val binder = object : IProduct.Stub() {
        override fun getProduct() = this@ProductService.getProduct()
        }
}