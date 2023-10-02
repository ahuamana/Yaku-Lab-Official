package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.fake.FirebaseUserFake
import com.ahuaman.data.dashboard.remote.FirebaseUserRemote
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val remote: FirebaseUserRemote,
    private val fake: FirebaseUserFake
) {
    suspend fun getInfoUser(email: String) = remote.getUser(email)

    fun getMedals() = fake.getMedals()
}