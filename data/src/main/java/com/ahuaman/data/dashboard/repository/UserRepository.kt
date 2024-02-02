package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.providers.UserProvider
import com.yakulab.domain.login.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userProvider: UserProvider
){
     fun create(user:User) = userProvider.createFlow(user)

    fun searchUserByEmail(email:String?) = userProvider.searchUserByEmailFlow(email)

    fun updatePoints(email:String?, points:Int) = userProvider.updatePointsFlow(email, points)

    fun updatePostBlocked(email:String?, list:MutableList<String>) = userProvider.updatePostBlockedFlow(email, list)

    fun updateUsersBlocked(email:String?, list:MutableList<String>) = userProvider.updateUsersBlockedFlow(email, list)

}