package com.paparazziteam.yakulap.presentation.bienvenida.viewmodels

import android.app.Application
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.getVersionName
import com.paparazziteam.yakulap.presentation.bienvenida.views.WelcomeActivity
import com.paparazziteam.yakulap.presentation.dashboard.views.DashboardActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ViewModelSplashScreen @Inject public constructor(
   private val application: Application,
   private val myPreferences: MyPreferences
): ViewModel() {

    private val _versionApp = MutableLiveData<String>()
    val versionApp:LiveData<String> = _versionApp

    private val _animationLogo = MutableLiveData<Animation>()
    val animationLogo :LiveData<Animation> = _animationLogo

    init {
        versionName()
        loadAnimationLogo()
        loadNextActivity()
    }

    private fun loadNextActivity() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (myPreferences.isLogin) {
                    isAlreadyLogin()
                } else {
                    application.startActivity(Intent(application, WelcomeActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) //Remove activities that have been created before
                    })

                }
            }
        }, 4000)
    }


    private fun isAlreadyLogin() {
        application.startActivity(Intent(application, DashboardActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) //Remove activities that have been created before
        })
    }

    private fun loadAnimationLogo() {
        var animacion = AnimationUtils.loadAnimation(application, R.anim.animacionsplash)
        _animationLogo.value = animacion
        animacion.start()
    }

    private fun versionName() {
        _versionApp.value = "${application.getString(R.string.app_version_code)} ${application.getVersionName()}"
    }

}
