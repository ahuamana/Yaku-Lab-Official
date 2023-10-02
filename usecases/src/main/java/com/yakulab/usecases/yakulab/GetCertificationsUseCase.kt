package com.yakulab.usecases.yakulab

import com.ahuaman.data.dashboard.repository.FirebaseUserRepository
import javax.inject.Inject

class GetCertificationsUseCase @Inject constructor(
    private val repository: FirebaseUserRepository
) {

    suspend operator fun invoke() = repository.getCertifications()
}