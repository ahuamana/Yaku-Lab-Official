package com.paparazziteam.yakulap.helper.application

import android.graphics.Color
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreferences @Inject constructor() {

    val prefs = CacheData()

    //PREFERENCES STRING
    var email: String
        get()      = prefs.getString("email_login", "")
        set(value) = prefs.setString("email_login", value)

    var lastName: String
        get()      = prefs.getString("lastName", "")
        set(value) = prefs.setString("lastName", value)

    var firstName: String
        get()      = prefs.getString("firstName", "")
        set(value) = prefs.setString("firstName", value)

    var savePostsBlocked:String
        get()      = prefs.getString("postBlocked", "")
        set(value) = prefs.setString("postBlocked", value)

    var saveUsersBlocked:String
        get()      = prefs.getString("usersBlocked", "")
        set(value) = prefs.setString("usersBlocked", value)

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