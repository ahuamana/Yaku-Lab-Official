package com.paparazziteam.yakulab.binding.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.gson.Gson
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.getDataColumn
import com.paparazziteam.yakulab.binding.helper.isDownloadsDocument
import com.paparazziteam.yakulab.binding.helper.isExternalStorageDocument
import com.paparazziteam.yakulab.binding.helper.isMediaDocument
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
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



fun convertStringToList(text: String, unoMenos: Boolean = false): List<String> {
    val newList: MutableList<String> = mutableListOf()
    val partir = if (unoMenos) 0 else 1
    val list = text.substring(partir, text.length - partir).split(",")
    list.forEach {
        newList.add(it.trim())
    }
    return newList
}

fun String.onlyOneSpace(): String {
    val re = Regex("\\s+")
    return re.replace(this, " ")
}

@Throws(IOException::class)
fun drawableFromUrl(url: String?): Drawable? {
    val x: Bitmap
    val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
    connection.connect()
    val input: InputStream = connection.getInputStream()
    x = BitmapFactory.decodeStream(input)
    return BitmapDrawable(Resources.getSystem(), x)
}

fun tintDrawable(drawable: Drawable, @ColorInt color: Int = MyPreferences().colorSecundario): Drawable {
    (drawable as? VectorDrawableCompat)
        ?.apply { setTintList(ColorStateList.valueOf(color)) }
        ?.let { return it }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        (drawable as? VectorDrawable)
            ?.apply { setTintList(ColorStateList.valueOf(color)) }
            ?.let { return it }
    }

    val wrappedDrawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(wrappedDrawable, color)
    return DrawableCompat.unwrap(wrappedDrawable)
}


val jsonBuilder = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    isLenient = true
}

inline fun <reified T> toJson(value : T) = jsonBuilder.encodeToString(value)
inline fun <reified T> fromJson(json: String) : T {
    return jsonBuilder.decodeFromString(json)
}

//using Gson
inline fun <reified T> fromJson(json: String, type: Class<T>) : T {
    return Gson().fromJson(json, type)
}


fun getRealPathFromURI(context: Context, uri: Uri): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri: Uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else return if ("content".equals(uri.scheme, ignoreCase = true)) {
        // Return the remote address
        if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        uri.path
    } else getRealPathFromURIDB(uri, context)
    return null
}

private fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.getAuthority()
}

private fun getRealPathFromURIDB(contentUri: Uri, context: Context): String? {
    val cursor: Cursor? = context.contentResolver.query(contentUri, null, null, null, null)
    return run {
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val realPath = index?.let { cursor.getString(it) }
        cursor?.close()
        realPath
    }
}

@SuppressLint("Range")
fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor!!.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

inline fun Context.toast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

inline fun Context.longToast(message: Int): Toast = Toast
    .makeText(this, message, Toast.LENGTH_LONG)
    .apply {
        show()
    }

fun Context.getVersionName(): String {
    var versionName = ""
    try {
        versionName = applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName,
            0
        ).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return versionName
}

fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.getActivity()
        else -> null
    }
}

fun String?.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun String?.convertFirstLetterToUpperCase(): String {
    return this?.substring(0, 1)?.toUpperCase(Locale.getDefault()) + this?.substring(1)
}

fun String?.convertFirstLetterToUpperCaseAndRestToLowerCase(): String {
    return this?.substring(0, 1)?.toUpperCase(Locale.getDefault()) + this?.substring(1)
        ?.toLowerCase(Locale.getDefault())
}

fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun getTimestamp(): String {
    return SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())
}

fun getTimestampUnix(): Long {
    return System.currentTimeMillis() / 1000
}
