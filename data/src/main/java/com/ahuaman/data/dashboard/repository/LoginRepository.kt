package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.remote.login.LoginRemoteDataSource
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource
) {
    fun loginAnonymous() = loginRemoteDataSource.loginAnonymous()

    fun logout() = loginRemoteDataSource.logout()

    fun loginWithEmail(email: String, pass: String) = loginRemoteDataSource.loginWithEmail(email, pass)

    fun createUser(email: String, pass: String) = loginRemoteDataSource.createUser(email, pass)

    fun getEmail(): String = loginRemoteDataSource.getEmail()

}