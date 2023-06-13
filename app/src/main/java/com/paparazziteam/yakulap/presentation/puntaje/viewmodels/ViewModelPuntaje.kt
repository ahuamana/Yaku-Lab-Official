package com.paparazziteam.yakulap.presentation.puntaje.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.helper.application.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ViewModelPuntaje @Inject constructor(
    val preferences:MyPreferences,
    @ApplicationContext val context: Context
):ViewModel() {

    private val _medal = MutableLiveData<Drawable>()
    fun getMedal(): LiveData<Drawable> = _medal

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading

    fun showMedal(points:Int = preferences.points){
        _loading.value = true
        getMedalla(points)?.let { _medal.value = it  }
        _loading.value = false
    }

    fun getMedalla(points: Int):Drawable? = when {
        points < 10 ->  context.getDrawable(R.drawable.bronze_medal)
        points in 11..50 -> context.getDrawable(R.drawable.silver_medal)
        points > 50 -> context.getDrawable(R.drawable.gold_medal)
        else -> context.getDrawable(R.drawable.bronze_medal)
    }
}