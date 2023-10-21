package com.ahuaman.data.dashboard.providers

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.paparazziteam.yakulab.binding.helper.CompressorBitmapImage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.*
import javax.inject.Inject

class ImageProvider @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private var mFirebaseStorage = FirebaseStorage.getInstance()
    var mStorage = mFirebaseStorage?.reference

    fun save(file: File): UploadTask? {
        val imageByte: ByteArray =
            CompressorBitmapImage.getImage(appContext, file.path, 500, 500) //
        val storage = mStorage?.child(Date().toString() + ".jpg")
        mStorage = storage
        return storage?.putBytes(imageByte)
    }

    fun getDownloadUri(): Task<Uri?>? {
        return mStorage?.downloadUrl
    }
}