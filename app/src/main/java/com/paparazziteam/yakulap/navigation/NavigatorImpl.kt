package com.paparazziteam.yakulap.navigation

import android.content.Context
import android.content.Intent
import com.ahuaman.feature_ar.MainARActivity
import com.paparazziteam.yakulab.binding.helper.navigator.Navigator
import com.paparazziteam.yakulap.presentation.login.views.LoginActivity
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    override fun navigateToLogin(context:Context, isUnique: Boolean) {
        val intent = Intent(context, LoginActivity::class.java).also {
            if(isUnique) it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun navigateToAR(context: Context, isUnique: Boolean) {
        val intent = Intent(context, MainARActivity::class.java).also {
            if(isUnique) it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}