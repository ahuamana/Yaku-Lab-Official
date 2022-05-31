package com.paparazziteam.yakulap.helper.applicacion

class MyPreferences {

    val prefs = CacheData()

    //PREFERENCES STRING
    var email_login: String
        get()      = prefs.getString("email_login", "")
        set(value) = prefs.setString("email_login", value)

    //PREFERENCES BOOLEAN
    var isLogin: Boolean
        get()      = prefs.getBoolean("isLogin", false)
        set(value) = prefs.setBoolean("isLogin", value)
}