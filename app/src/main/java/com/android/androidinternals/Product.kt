package com.android.androidinternals

import android.os.Parcel
import android.os.Parcelable

class Product() : Parcelable {

    var packageName = "Undefined"
    var pid = 0
    var user = "Undefined"

    constructor(parcel: Parcel) : this() {
        readFromParcel(parcel)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(outParcel: Parcel, p1: Int) {
        outParcel.writeString(packageName)
        outParcel.writeInt(pid)
        outParcel.writeString(user)
    }

    private fun readFromParcel(inParcel: Parcel) {
        packageName = inParcel.readString().toString()
        pid = inParcel.readInt()
        user = inParcel.readString().toString()
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}