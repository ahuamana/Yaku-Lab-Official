package com.paparazziteam.yakulap.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import com.paparazziteam.yakulab.binding.Constants
import com.paparazziteam.yakulab.binding.utils.FOLD_PRINC
import com.paparazziteam.yakulab.binding.utils.TAG
import com.paparazziteam.yakulab.binding.utils.toast
import com.paparazziteam.yakulap.root.ctx
import java.io.File
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

fun View.downloadData(
    url: String,
    carpetaDestino: String,
    tituloDoc: String,
    extencion: String,
    typeExcel: Boolean = false
){
    try {
        val listDownloadManager: ArrayList<Long> = ArrayList()
        val uri = Uri.parse(url)

        ctx.toast("Descarga en proceso...")
        val downloadManager: DownloadManager? = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?

        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss")
        val currentDateAndTime = df.format(c.time)
        val filePath = Environment.getExternalStorageDirectory().absolutePath + FOLD_PRINC + carpetaDestino
        val dir = File(filePath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "$tituloDoc-$currentDateAndTime$extencion")

        val downloadReference: Long?
        val request = DownloadManager.Request(uri)
        request.setTitle(tituloDoc)
        if(typeExcel) request.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(true)
        request.setVisibleInDownloadsUi(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) request.setDestinationInExternalPublicDir(
            if (extencion != Constants.EXT_JPG) Environment.DIRECTORY_PICTURES
            else Environment.DIRECTORY_DOCUMENTS, "$tituloDoc-$currentDateAndTime$extencion"
        )
        else request.setDestinationUri(Uri.fromFile(file))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) request.setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) else context.toast("Descarga Completa")
        downloadReference = downloadManager?.enqueue(request)

        listDownloadManager.add(downloadReference ?: 0)
    }catch (e: Exception){
        Log.e(TAG, "downloadData: $e")
    }
}