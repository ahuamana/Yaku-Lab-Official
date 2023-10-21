package com.ahuaman.data.dashboard.remote.login

interface LoginRemote {

    fun loginWithEmail(email: String?, pass: String?)
    fun loginAnonymous()
    fun logout()
}