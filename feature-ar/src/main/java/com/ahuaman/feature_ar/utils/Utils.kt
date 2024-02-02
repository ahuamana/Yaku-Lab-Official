package com.ahuaman.feature_ar.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri

fun findActivity(context: Context): Activity? {
    var currentContext = context
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

fun ByteArray.toFile(context: Context, fileName: String = "model.glb"): Uri {
    val file = context.getFileStreamPath(fileName)
    file.writeBytes(this)
    return Uri.parse("file://${file.absolutePath}")
}