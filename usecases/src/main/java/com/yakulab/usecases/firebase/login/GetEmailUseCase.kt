package com.yakulab.usecases.firebase.login

import com.ahuaman.data.dashboard.repository.FirebaseLoginRepository
import javax.inject.Inject

class GetEmailUseCase @Inject constructor(
    private val repository: FirebaseLoginRepository
) {

    fun invoke() = repository.getEmailLogged()

}