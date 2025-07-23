package com.android.androidinternals

object PackageInfoKey {
    const val PACKAGE_NAME = "package_name"
    const val PID = "pid"
    const val USER = "user"
}

data class PackageData(
    val packageName: String = "",
    val pid: Int = 0,
    val user: String = ""
)