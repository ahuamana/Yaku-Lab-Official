package com.yakulab.usecases.firebase.login

import com.ahuaman.data.dashboard.repository.LoginRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    val loginRepository: LoginRepository
) {
    fun invoke(email: String, pass: String) = loginRepository.createUser(email, pass)
}