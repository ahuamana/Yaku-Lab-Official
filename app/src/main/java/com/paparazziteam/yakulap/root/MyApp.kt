package com.paparazziteam.yakulap.root

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.paparazziteam.yakulap.BuildConfig.DEBUG
import com.testfairy.TestFairy
import dagger.hilt.android.HiltAndroidApp

lateinit var ctx      : Context

@HiltAndroidApp
open class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        if(DEBUG) {
            println("DEBUG MODE")
            TestFairy.begin(this,"SDK-nQlmn0dn")
        }

        FirebaseApp.initializeApp(this)
        ctx   = this
    }
}