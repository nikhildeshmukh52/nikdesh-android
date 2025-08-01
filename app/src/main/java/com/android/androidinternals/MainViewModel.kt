package com.android.androidinternals

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    private var file: File? = null

    fun saveToInternalStorage(context: Context, bitmap: Bitmap): Boolean {
        val internalDirectory = context.filesDir
        val directory = File(internalDirectory, "AndroidInternalStorage")
        if(!directory.exists()) {
            directory.mkdir()
        }
        file = File(directory, UUID.randomUUID().toString() + ".jpg")
        return file!!.outputStream().use { outputStream ->
            val compressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            compressed
        }
    }

    suspend fun readImageFromInternalStorage(): Boolean {
        return withContext(Dispatchers.IO) {
            file?.let {
                file!!.inputStream().use { inputStream ->
                    val bytes = inputStream.readBytes()
                    _bitmap.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
                true
            } ?: false
        }
    }
}