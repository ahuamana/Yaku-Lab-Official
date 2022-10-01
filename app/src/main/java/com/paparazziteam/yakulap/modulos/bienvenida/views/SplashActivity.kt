package com.paparazziteam.yakulap.modulos.bienvenida.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivitySplashBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.modulos.bienvenida.viewmodels.ViewModelSplashScreen
import com.paparazziteam.yakulap.modulos.dashboard.views.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val _viewmodel:ViewModelSplashScreen by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash)

        binding.apply {
            //lifecycleOwner = this.lifecycleOwner
            viewmodel = _viewmodel
        }
    }
}