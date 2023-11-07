package com.paparazziteam.yakulab.binding.helper.navigator

import android.content.Context
import android.os.Bundle

interface Navigator {
    fun navigateToLogin(context: Context, isUnique: Boolean = false)

    fun navigateToAR(context: Context, isUnique: Boolean = false, bunble: Bundle? = null)
}