package com.yakulab.usecases.firebase.user

import com.ahuaman.data.dashboard.providers.UserProvider
import com.yakulab.domain.login.User
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userProvider: UserProvider
) {

    fun invoke(user:User) = userProvider.createFlow(user)

}