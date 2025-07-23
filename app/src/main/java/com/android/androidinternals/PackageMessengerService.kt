package com.android.androidinternals

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger

class PackageMessengerService: Service() {

    companion object {
        const val REQUEST_PACKAGE_INFO = 1
    }
    inner class PackageHandler: Handler(Looper.getMainLooper()){

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (msg.what == REQUEST_PACKAGE_INFO) {
                val message = Message.obtain(this@PackageHandler, REQUEST_PACKAGE_INFO)
                val bundle = Bundle()
                bundle.putString(PackageInfoKey.PACKAGE_NAME, "com.android.androidinternals" + ".section${(0..10).random()}")
                bundle.putInt(PackageInfoKey.PID, (0..100).random())
                bundle.putString(PackageInfoKey.USER, "user" + "${(0..10).random()}")
                message.data = bundle
                msg.replyTo.send(message)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        val messenger = Messenger(PackageHandler())
        return messenger.binder
    }
}