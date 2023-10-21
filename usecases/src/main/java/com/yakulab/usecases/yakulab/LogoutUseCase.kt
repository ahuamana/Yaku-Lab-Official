package com.yakulab.usecases.yakulab

import com.ahuaman.data.dashboard.repository.LoginRepository
import java.util.prefs.Preferences
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {

    operator fun invoke() {
        loginRepository.logout()
    }
}