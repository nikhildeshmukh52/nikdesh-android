package com.android.androidinternals.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.androidinternals.ContactProviderClient
import com.android.androidinternals.provider.ContactProvider
import com.android.androidinternals.room.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(application: Application): AndroidViewModel(application) {
    private val contactProviderClient = ContactProviderClient(application.applicationContext)

    private val _contact = MutableStateFlow(
        Contact(99,
        "Undefined", "Undefined",
            "Undefined","Undefined", "Undefined"))
    val contact = _contact.asStateFlow()

    init {
        loadFirstContact()
    }

    @SuppressLint("Range")
    private fun loadFirstContact() {
        viewModelScope.launch {
            _contact.value = contactProviderClient.queryContact()[0]
        }
    }
}