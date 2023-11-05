package com.paparazziteam.yakulab.binding.helper.navigator

import android.content.Context

interface Navigator {
    fun navigateToLogin(context: Context, isUnique: Boolean = false)

    fun navigateToAR(context: Context, isUnique: Boolean = false)
}