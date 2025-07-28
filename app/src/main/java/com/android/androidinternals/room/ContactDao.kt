package com.android.androidinternals.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(vararg contacts: Contact)

    @Query("SELECT * FROM contact")
    fun getContactList(): Cursor

    @Query("SELECT * FROM contact WHERE contactId = :rowId")
    fun getContact(rowId: Int): Cursor
}