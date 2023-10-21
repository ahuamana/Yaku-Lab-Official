package com.paparazziteam.yakulab.binding.utils

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

suspend fun downloadImage(context: Context,imageUrl: String, fileName: String) = withContext(Dispatchers.IO) {
    val request = DownloadManager.Request(Uri.parse(imageUrl))
        .setTitle(fileName)
        .setDescription("Descargando imagen")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}


fun getUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
    return try {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            createUUID(),
            null
        )
        val uri = Uri.parse(path)
        uri
    } catch (e: Exception) {
        Log.e("TAG", "getUriFromBitmap: ${e.message}")
        Uri.EMPTY
    }
}