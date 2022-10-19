package com.paparazziteam.yakulap.helper

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

object CompressorBitmapImage {
    /*
     * Metodo que permite comprimir imagenes y transformarlas a bitmap
     */
    fun getImage(
        ctx: Context?,
        path: String?,
        width: Int,
        height: Int
    ): ByteArray {
        println("Path received to compress: $path")
        val file_thumb_path = File(path)
        var thumb_bitmap: Bitmap? = null
        try {
            thumb_bitmap = Compressor(ctx)
                .setMaxWidth(width)
                .setMaxHeight(height)
                .setQuality(75)
                .compressToBitmap(file_thumb_path)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val baos = ByteArrayOutputStream()
        thumb_bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        return baos.toByteArray()
    }
}

fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                  selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        if (uri == null) return null
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
            null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}