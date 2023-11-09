package com.paparazziteam.yakulap.presentation.puntaje.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulap.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ViewModelPuntaje @Inject constructor(
    val preferences: MyPreferences
):ViewModel() {

    private val _medal = MutableLiveData<Drawable>()
    fun getMedal(): LiveData<Drawable> = _medal

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading

    fun showMedal(points:Int = preferences.points, resources: Resources){
        _loading.value = true
        getMedalla(points, resources)?.let { _medal.value = it  }
        _loading.value = false
    }

    private fun getMedalla(points: Int, resources: Resources):Drawable? = when {
        points < 10 -> ResourcesCompat.getDrawable(resources, R.drawable.bronze_medal,null)
        points in 11..50 -> ResourcesCompat.getDrawable(resources,R.drawable.silver_medal, null)
        points > 50 -> ResourcesCompat.getDrawable(resources,R.drawable.gold_medal, null)
        else -> ResourcesCompat.getDrawable(resources,R.drawable.bronze_medal, null)
    }
}