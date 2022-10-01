package com.paparazziteam.yakulap.modulos.bienvenida.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.design.FadePageTransfomer
import com.paparazziteam.yakulap.helper.getVersionName
import com.paparazziteam.yakulap.modulos.bienvenida.MyPageAdapter
import com.paparazziteam.yakulap.modulos.bienvenida.fragments.IntroFragment
import com.paparazziteam.yakulap.modulos.bienvenida.views.WelcomeActivity
import com.paparazziteam.yakulap.modulos.dashboard.views.DashboardActivity
import com.paparazziteam.yakulap.modulos.login.providers.LoginProvider
import com.paparazziteam.yakulap.modulos.login.views.LoginActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ViewModelWelcome @Inject public constructor(
   private val application: Application,
   private val myPreferences: MyPreferences
): ViewModel() {

    private val _versionApp = MutableLiveData<String>()
    val versionApp:LiveData<String> = _versionApp

    private val _animationLogo = MutableLiveData<Animation>()
    val animationLogo :LiveData<Animation> = _animationLogo

    private val _fragments = MutableLiveData<List<Fragment>>()
    val fragments :LiveData<List<Fragment>> = _fragments

    private val _nextActivity = MutableLiveData<Boolean>()
    val nextActivity :LiveData<Boolean> = _nextActivity

    init {

    }

    fun onClickStart(){
        _nextActivity.value = true
        _nextActivity.value = false
    }

    fun getFragments(){
        val fList = arrayListOf<Fragment>()
        fList.add(
            IntroFragment().newInstance(R.drawable.intro_primera,
                application.getString(R.string.intro_primera),
                application.getString(R.string.intro_desc_primera)))

        fList.add(
            IntroFragment().newInstance(R.drawable.intro_segunda,
                application.getString(R.string.intro_segunda),
                application.getString(R.string.intro_desc_segunda)))

        fList.add(
            IntroFragment().newInstance(R.drawable.intro_tercera,
                application.getString(R.string.intro_tercera),
                application.getString(R.string.intro_desc_tercera)))

        _fragments.value = fList
    }



}
