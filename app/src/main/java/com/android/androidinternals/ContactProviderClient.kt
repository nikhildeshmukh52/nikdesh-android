package com.android.androidinternals

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.net.toUri
import com.android.androidinternals.provider.ContactProvider
import com.android.androidinternals.room.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactProviderClient(private val context: Context) {

    @SuppressLint("Range", "Recycle")
    suspend fun queryContact(): List<Contact> {
        return withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(
                "${ContactProvider.CONTENT_URI}".toUri(),
                null,
                null,
                null,
                null
            )
            val contactList = mutableListOf<Contact>()
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex("contactId"))
                    val firstName = cursor.getString(cursor.getColumnIndex("firstName"))
                    val lastName = cursor.getString(cursor.getColumnIndex("lastName"))
                    val phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"))
                    val city = cursor.getString(cursor.getColumnIndex("city"))
                    val country = cursor.getString(cursor.getColumnIndex("country"))
                    contactList.add(Contact(id, firstName, lastName, phoneNumber, city, country))
                }
            }
            contactList
        }
    }
}