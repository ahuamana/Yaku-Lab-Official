package com.yakulab.usecases.firebase.user

import com.ahuaman.data.dashboard.repository.UserRepository
import javax.inject.Inject

class UpdatePostBlockedUseCase @Inject constructor(
    private val repository: UserRepository
) {

    fun invoke(email: String?, list: MutableList<String>) = repository.updatePostBlocked(email, list)

}