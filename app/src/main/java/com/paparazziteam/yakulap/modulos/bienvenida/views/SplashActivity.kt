package com.paparazziteam.yakulap.modulos.bienvenida.views

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivitySplashBinding
import com.paparazziteam.yakulap.helper.applicacion.MyPreferences
import com.paparazziteam.yakulap.modulos.login.LoginActivity
import java.util.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadNextActivity()
        loadAnimationLogo()

    }

    private fun loadAnimationLogo() {
        var animacion = AnimationUtils.loadAnimation(this, R.anim.animacionsplash)
        binding.imglogo.startAnimation(animacion)
    }

    private fun loadNextActivity() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (MyPreferences().isLogin) {
                    goToLogin()
                } else {
                    startActivity(Intent(applicationContext, WelcomeActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) //Remove activities that have been created before
                    })

                }
            }
        }, 4000)
    }

    private fun goToLogin() {
        startActivity(Intent(applicationContext, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) //Remove activities that have been created before
        })
    }
}