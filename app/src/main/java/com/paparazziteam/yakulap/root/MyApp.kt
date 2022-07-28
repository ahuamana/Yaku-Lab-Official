package com.paparazziteam.yakulap.root

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

lateinit var ctx      : Context
open class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this);
        ctx   = this
    }
}