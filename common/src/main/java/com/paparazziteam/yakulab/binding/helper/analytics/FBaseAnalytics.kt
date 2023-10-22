package com.paparazziteam.yakulab.binding.helper.analytics

import android.content.Context

interface FBaseAnalytics {

    fun logEvent(name: String, params: Map<String, String>)

    fun logLoginEvent()

    fun logSignUpEvent()

    fun shareEvent()

    fun startRegistrationEvent()

    fun completeRegistrationEvent()
}