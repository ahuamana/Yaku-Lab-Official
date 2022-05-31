package com.paparazziteam.yakulap.root

import android.app.Application
import android.content.Context

lateinit var ctx      : Context
open class MyApp(): Application() {
    override fun onCreate() {
        super.onCreate()
        ctx   = this
    }
}