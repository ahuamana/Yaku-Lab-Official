package com.yakulab.usecases.inaturalist.firebase

import com.ahuaman.data.dashboard.repository.FirebaseLoginRepository
import javax.inject.Inject

class getEmailLoggedUseCase @Inject constructor(
    private val repository: FirebaseLoginRepository
) {

    fun invoke() = repository.getEmailLogged()

}