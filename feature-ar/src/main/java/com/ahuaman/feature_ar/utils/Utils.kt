package com.ahuaman.feature_ar.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

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