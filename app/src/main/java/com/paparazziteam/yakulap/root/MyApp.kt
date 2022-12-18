package com.paparazziteam.yakulap.root

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.testfairy.TestFairy
import dagger.hilt.android.HiltAndroidApp

lateinit var ctx      : Context

@HiltAndroidApp
open class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        TestFairy.begin(this,"SDK-nQlmn0dn")
        ctx   = this
    }
}