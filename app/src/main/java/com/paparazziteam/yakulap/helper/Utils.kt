package com.paparazziteam.yakulap.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.paparazziteam.yakulap.helper.Constants.EXT_JPG
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.application.toast
import com.paparazziteam.yakulap.root.ctx
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private var appContext: Context? = null

val TAG        = "YAKU_APP"
val FOLD_PRINC     = "/Yakulap"


val application: Context
    get() = appContext ?: initAndGetAppCtxWithReflection()

@SuppressLint("PrivateApi")
private fun initAndGetAppCtxWithReflection(): Context {
    val activityThread = Class.forName("android.app.ActivityThread")
    val ctx = activityThread.getDeclaredMethod("currentApplication").invoke(null) as Context
    appContext = ctx
    return ctx
}

fun setColorToStatusBar(activity: Activity, color: Int = MyPreferences().color) {
    val window = activity.window
    val hsv = FloatArray(3)
    var darkColor: Int = color

    Color.colorToHSV(darkColor, hsv)
    hsv[2] *= 0.8f // value component
    darkColor = Color.HSVToColor(hsv)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = darkColor //Define color
    }
}


fun hideKeyboardActivity(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun isConnected(context:Context):Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    try {
        if (activeNetwork != null) {
            val caps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.getNetworkCapabilities(cm.activeNetwork)
            } else {

            }
        }
    } catch (e: Exception) {
        Log.e("ERROR", "" + e.message)
    }
    return activeNetwork != null && activeNetwork.isConnected
}


fun replaceFirstCharInSequenceToUppercase(text: String): String {
    if(text.contains(" ")){
        val words = text.trim().split(" ").toTypedArray()
        val wordsUppercase: MutableList<String?> =
            ArrayList()
        for (data in words) {
            val upperString = data.substring(0, 1)
                .uppercase(Locale.getDefault()) + data.substring(1)
                .lowercase(Locale.getDefault())
            //Log.e("REPLACE","DATA: "+ upperString);
            wordsUppercase.add(upperString)
        }
        return TextUtils.join(" ", wordsUppercase)
    }else{
        return text.replaceFirstChar { it.uppercase() }
    }
    //Log.e("REPLACE","DATA: "+ datafinal);
}


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
            if (extencion != EXT_JPG) Environment.DIRECTORY_PICTURES
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

fun convertStringToList(text: String, unoMenos: Boolean = false): List<String> {
    val newList: MutableList<String> = mutableListOf()
    val partir = if (unoMenos) 0 else 1
    val list = text.substring(partir, text.length - partir).split(",")
    list.forEach {
        newList.add(it.trim())
    }
    return newList
}


