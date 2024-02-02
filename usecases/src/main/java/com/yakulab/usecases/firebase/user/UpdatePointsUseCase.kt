package com.yakulab.usecases.firebase.user

import com.ahuaman.data.dashboard.repository.UserRepository
import javax.inject.Inject

class UpdatePointsUseCase @Inject constructor(
    private val repository: UserRepository
) {

    fun invoke(email: String?, points: Int) = repository.updatePoints(email, points)

}
