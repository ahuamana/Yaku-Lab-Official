package com.ahuaman.data.dashboard.remote.login

import com.ahuaman.data.dashboard.providers.LoginProvider
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(
    private val loginProvider: LoginProvider
): LoginRemote {
    override fun loginWithEmail(email: String?, pass: String?) {
        loginProvider.loginEmail(email?:"", pass?:"")
    }

    override fun loginAnonymous() {
        loginProvider.loginAnonymous()
    }

    override fun logout() {
        loginProvider.signOut()
    }


}