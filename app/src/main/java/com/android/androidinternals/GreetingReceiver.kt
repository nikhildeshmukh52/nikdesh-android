package com.android.androidinternals

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class GreetingReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        println("Greeting Received")
    }
}