package com.paparazziteam.yakulap.presentation.bienvenida.views

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivitySplashBinding
import com.paparazziteam.yakulap.presentation.bienvenida.viewmodels.ViewModelSplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val _viewmodel:ViewModelSplashScreen by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        binding.apply {
            //lifecycleOwner = this.lifecycleOwner
            viewmodel = _viewmodel
        }
    }
}