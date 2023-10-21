package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.remote.FirebaseLoginRemote
import javax.inject.Inject

class FirebaseLoginRepository @Inject constructor(
    private val remote: FirebaseLoginRemote
) {
    fun getEmailLogged(): String {
        return remote.getEmail()
    }
}