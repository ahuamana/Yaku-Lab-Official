package com.ahuaman.data.dashboard.remote.login

import com.ahuaman.data.dashboard.providers.LoginProvider
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(
    private val loginProvider: LoginProvider
): LoginRemote {
    override fun loginWithEmail(email: String, pass: String) = loginProvider.loginEmail(email, pass)

    override fun loginAnonymous() = loginProvider.loginAnonymous()

    override fun logout() {
        loginProvider.signOut()
    }

    override fun createUser(email: String, pass: String) = loginProvider.createUser(email, pass)

    override fun getEmail(): String = loginProvider.getEmail()


}