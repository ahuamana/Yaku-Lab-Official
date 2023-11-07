package com.ahuaman.data.dashboard.remote.login

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface LoginRemote {

    fun loginWithEmail(email: String, pass: String) : Flow<AuthResult>
    fun loginAnonymous() : Flow<AuthResult>
    fun logout()

    fun createUser(email: String, pass: String) : Flow<AuthResult>

    fun getEmail(): String
}