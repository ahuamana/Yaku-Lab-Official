package com.yakulab.usecases.yakulab

import com.ahuaman.data.dashboard.repository.LoginRepository
import javax.inject.Inject

class LoginWithEmailUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    operator fun invoke(email: String, pass: String) {
        loginRepository.loginWithEmail(email, pass)
    }
}