package com.android.androidinternals.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.Room
import com.android.androidinternals.room.Contact
import com.android.androidinternals.room.ContactDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ContactProvider: ContentProvider() {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    companion object {
        const val AUTHORITY = "com.android.androidinternals.provider"
        const val PATH = "Contact"
        val CONTENT_URI = "content://$AUTHORITY/$PATH".toUri()
        const val TABLE_ID = 0
        const val ROW_ID = 1

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH, TABLE_ID)
            addURI(AUTHORITY, "${PATH}/#", ROW_ID)
        }
    }

    private var contactDB: ContactDatabase? = null

    override fun onCreate(): Boolean {
        if (context != null) {
            contactDB = Room.
            databaseBuilder(context!!.applicationContext!!, ContactDatabase::class.java, "contactDB")
                .build()
            populateContactDatabase()
        }

        return true
    }

    private fun populateContactDatabase() {
        contactDB?.let {
            coroutineScope.launch {
                contactDB!!.contactDao().insert(
                    Contact(10, "John", "Cena",
                        "+1234567899", "NewYork", "USA"),
                    Contact(20, "The", "Rock",
                        "+1234567499", "Maimi", "USA"),
                    Contact(30, "Rey", "Mestario",
                        "+1234368490", "Venice", "Italy")
                )
            }
        }
    }

    override fun query(
       uri: Uri,
       columns: Array<out String>?,
       selection: String?,
       selectionArgs: Array<out String>?,
       sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TABLE_ID -> {
                contactDB?.contactDao()?.getContactList()
            }

            ROW_ID -> {
                val pathSegment = uri.lastPathSegment
                pathSegment?.let {
                    contactDB?.contactDao()?.getContact(it.toInt())
                }
            }
            else -> { throw IllegalArgumentException()}
        }
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)) {
            TABLE_ID -> "vnd.android.cursor.dir/com.android.androidinternals.provider.Contact"
            ROW_ID -> "vnd.android.cursor.item/com.android.androidinternals.provider.Contact"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int = 0
    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int = 0
}