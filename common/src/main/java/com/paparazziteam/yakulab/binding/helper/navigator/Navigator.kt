package com.paparazziteam.yakulab.binding.helper.navigator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher

interface Navigator {
    fun navigateToLogin(context: Context, isUnique: Boolean = false)

    fun navigateToAR(context: Context, isUnique: Boolean = false, bundle: Bundle? = null)

    fun navigateToARWithBundleAndResultLauncher(context: Context, isUnique: Boolean = false, bundle: Bundle? = null, launcher: ActivityResultLauncher<Intent>)
}