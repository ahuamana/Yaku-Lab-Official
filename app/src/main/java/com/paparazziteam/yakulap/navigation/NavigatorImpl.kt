package com.paparazziteam.yakulap.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.ahuaman.feature_ar.presentation.MainARActivity
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

    override fun navigateToAR(context: Context, isUnique: Boolean, bundle:Bundle?) {
        val intent = Intent(context, MainARActivity::class.java).also {
            if(isUnique) it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intentt ->
            bundle?.let {
                intentt.putExtras(bundle)
            }
        }

        context.startActivity(intent)
    }

    //open ARArActivity with bundle and pass  ActivityResultLauncher
    override fun navigateToARWithBundleAndResultLauncher(context: Context, isUnique: Boolean, bundle: Bundle?, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(context, MainARActivity::class.java).also {
            if(isUnique) it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intentt ->
            bundle?.let {
                intentt.putExtras(bundle)
            }
        }

        launcher.launch(intent)
    }
}