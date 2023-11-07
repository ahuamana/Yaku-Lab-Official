package com.yakulab.usecases.firebase.login

import com.ahuaman.data.dashboard.repository.LoginRepository
import javax.inject.Inject

class GetLoginWithEmailUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    fun invoke(email: String, pass: String) = repository.loginWithEmail(email, pass)
}