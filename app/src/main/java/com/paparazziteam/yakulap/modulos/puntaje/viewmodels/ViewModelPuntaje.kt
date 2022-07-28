package com.paparazziteam.yakulap.modulos.puntaje.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider
import com.paparazziteam.yakulap.modulos.puntaje.pojo.TypeMedal
import com.paparazziteam.yakulap.root.ctx
import java.lang.reflect.Type

class ViewModelPuntaje {
    var preferences = MyPreferences()

    private val _medal = MutableLiveData<Drawable>()
    fun getMedal(): LiveData<Drawable> = _medal

    fun showMedal(points:Int = preferences.points){
        //Asignar Medalla
        if (points < 10) {

            _medal.value = ctx.getDrawable(R.drawable.bronze_medal)
        } else {
            if (points in 11..50) {
                _medal.value = ctx.getDrawable(R.drawable.silver_medal)
            } else {
                if (points > 50) {
                    _medal.value = ctx.getDrawable(R.drawable.gold_medal)
                }else{
                    _medal.value = ctx.getDrawable(R.drawable.bronze_medal)
                }
            }
        }

    }


    companion object Singleton{
        private var instance: ViewModelPuntaje? = null

        fun getInstance(): ViewModelPuntaje =
            instance ?: ViewModelPuntaje(
                //local y remoto
            ).also {
                instance = it
            }

        fun destroyInstance(){
            instance = null
        }
    }
}