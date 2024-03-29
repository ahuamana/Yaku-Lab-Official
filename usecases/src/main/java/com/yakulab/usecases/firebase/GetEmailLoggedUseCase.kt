package com.yakulab.usecases.firebase

import com.ahuaman.data.dashboard.repository.FirebaseLoginRepository
import javax.inject.Inject

class GetEmailLoggedUseCase @Inject constructor(
    private val repository: FirebaseLoginRepository
) {

    fun invoke() = repository.getEmailLogged()

}