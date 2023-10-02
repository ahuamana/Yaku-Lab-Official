package com.yakulab.usecases.yakulab

import com.ahuaman.data.dashboard.repository.FirebaseUserRepository
import javax.inject.Inject

class GetMedalsUseCase @Inject constructor(
    private val repository: FirebaseUserRepository
) {
    suspend fun invoke() = repository.getMedals()
}