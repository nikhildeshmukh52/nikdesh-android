package com.android.androidinternals.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey
    val contactId: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val city: String,
    val country: String
)