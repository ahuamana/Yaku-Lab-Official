package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.remote.FirebaseUserRemote
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val remote: FirebaseUserRemote
) {
    suspend fun getInfoUser(email: String) = remote.getUser(email)
}