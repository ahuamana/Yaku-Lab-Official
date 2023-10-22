package com.paparazziteam.yakulap.firebase.analytics


import com.google.firebase.analytics.FirebaseAnalytics
import com.paparazziteam.yakulab.binding.helper.analytics.FBaseAnalytics
import javax.inject.Inject

class FBaseAnalyticsImpl @Inject constructor(
    private var firebaseAnalytics: FirebaseAnalytics
) : FBaseAnalytics {

    override fun logEvent(name: String, params: Map<String, String>) {
        firebaseAnalytics.logEvent(name, null)
    }

    override fun logLoginEvent() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)
    }

    override fun logSignUpEvent() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, null)
    }

    override fun shareEvent() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, null)
    }

    override fun startRegistrationEvent() {
        firebaseAnalytics.logEvent(CustomEvent.START_REGISTRATION.name, null)
    }

    override fun completeRegistrationEvent() {
        firebaseAnalytics.logEvent(CustomEvent.COMPLETE_REGISTRATION.name, null)
    }

    private fun test(){
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)
    }

    enum class CustomEvent() {
        LOGIN,
        SIGN_UP,
        SHARE,
        START_REGISTRATION,
        COMPLETE_REGISTRATION
    }

}