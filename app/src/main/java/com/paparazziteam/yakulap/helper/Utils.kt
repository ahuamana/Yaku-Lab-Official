package com.paparazziteam.yakulap.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.paparazziteam.yakulap.helper.application.MyPreferences
import java.util.*

private var appContext: Context? = null

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