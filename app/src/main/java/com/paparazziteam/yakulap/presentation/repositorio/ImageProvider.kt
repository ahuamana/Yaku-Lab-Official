package com.paparazziteam.yakulap.presentation.repositorio

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.paparazziteam.yakulap.helper.CompressorBitmapImage

import com.paparazziteam.yakulap.root.ctx
import java.io.File
import java.util.*
import javax.inject.Inject

class ImageProvider @Inject constructor() {

    private var mFirebaseStorage = FirebaseStorage.getInstance()
    var mStorage = mFirebaseStorage?.reference

    fun save(file: File): UploadTask? {
        val imageByte: ByteArray =
            CompressorBitmapImage.getImage(ctx, file.path, 500, 500) //
        val storage = mStorage?.child(Date().toString() + ".jpg")
        mStorage = storage
        return storage?.putBytes(imageByte)
    }

    fun getDownloadUri(): Task<Uri?>? {
        return mStorage?.downloadUrl
    }
}