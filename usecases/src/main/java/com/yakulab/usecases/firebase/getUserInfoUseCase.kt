package com.yakulab.usecases.firebase

import com.ahuaman.data.dashboard.repository.FirebaseUserRepository
import javax.inject.Inject

class getUserInfoUseCase @Inject constructor(
    private val repository: FirebaseUserRepository
) {
    suspend fun invoke(email:String) = repository.getInfoUser(email)
}