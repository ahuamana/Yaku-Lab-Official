package com.paparazziteam.yakulap.helper.application

import android.graphics.Color
import com.paparazziteam.yakulap.helper.INT_DEFAULT
import com.paparazziteam.yakulap.helper.toJson
import dagger.hilt.EntryPoint
import dagger.hilt.android.migration.CustomInject
import javax.inject.Inject


class MyPreferences @Inject constructor() {

    val prefs = CacheData()

    //PREFERENCES STRING
    var email_login: String
        get()      = prefs.getString("email_login", "")
        set(value) = prefs.setString("email_login", value)

    var lastName: String
        get()      = prefs.getString("lastName", "")
        set(value) = prefs.setString("lastName", value)

    var firstName: String
        get()      = prefs.getString("firstName", "")
        set(value) = prefs.setString("firstName", value)

    var postBlocked:String
        get()      = prefs.getString("postBlocked", "")
        set(value) = prefs.setString("postBlocked", value)

    //PREFERENCES BOOLEAN
    var isLogin: Boolean
        get()      = prefs.getBoolean("isLogin", false)
        set(value) = prefs.setBoolean("isLogin", value)

    //PREFERENCES INTEGER
    var color: Int
        get() = prefs.getInt("color",  Color.parseColor("#ff0066"))
        set(value) = prefs.setInt("color", value)

    var colorSecundario: Int
        get() = prefs.getInt("colorSecundario",  Color.parseColor("#fcc52d"))
        set(value) = prefs.setInt("colorSecundario", value)

    var points: Int
        get() = prefs.getInt("points",  0)
        set(value) = prefs.setInt("points", value)


    fun removeLoginData(){
        prefs.remove("isLogin")
        prefs.remove("email_login")
        prefs.remove("points")
        prefs.remove("postBlocked")
    }

    fun clearData(){
        prefs.remove("isLogin")
        prefs.remove("email_login")
        prefs.remove("points")
        prefs.remove("postBlocked")
        prefs.remove("lastName")
        prefs.remove("firstName")
    }


}