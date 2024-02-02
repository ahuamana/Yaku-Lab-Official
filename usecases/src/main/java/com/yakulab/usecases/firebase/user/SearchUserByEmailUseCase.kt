package com.yakulab.usecases.firebase.user

import com.ahuaman.data.dashboard.repository.UserRepository
import javax.inject.Inject

class SearchUserByEmailUseCase @Inject constructor(
    private val repository: UserRepository
) {

    fun invoke(email: String?) = repository.searchUserByEmail(email)

}
