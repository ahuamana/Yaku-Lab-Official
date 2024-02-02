package com.ahuaman.data.dashboard.providers

import com.ahuaman.data.dashboard.di.DashboardModule
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.yakulab.domain.login.User
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProvider @Inject constructor(
    @DashboardModule.UsersCollection private val mCollection:CollectionReference
) {



    fun create(user: User): Task<Void> {
        return mCollection.document(user.email?:"").set(user)
    }

    fun createFlow(user: User) = flow {
        mCollection.document(user.email?:"").set(user).await()
        emit(true)
    }

    fun searchUserByEmail(email: String?): Task<DocumentSnapshot> {
        return mCollection.document(email?:"").get()
    }

    fun searchUserByEmailFlow(email: String?) = flow {
        val user = mCollection.document(email ?: "").get().await().toObject(User::class.java)
        emit(user)
    }

    fun updatePoints(email: String?, points: Int): Task<Void?>? {
        return mCollection.document(email?:"").update("points", points)
    }

    fun updatePointsFlow(email: String?, points: Int) = flow {
        mCollection.document(email?:"").update("points", points).await()
        emit(true)
    }

    fun updatePostBlocked(email: String?, list: MutableList<String>): Task<Void?>? {
        return mCollection.document(email?:"").update("post_blocked", list)
    }

    fun updatePostBlockedFlow(email: String?, list: MutableList<String>) = flow {
        mCollection.document(email?:"").update("post_blocked", list).await()
        emit(true)
    }

    fun updateUsersBlocked(email: String?, list: MutableList<String>): Task<Void?>? {
        return mCollection.document(email?:"").update("users_blocked", list)
    }

    fun updateUsersBlockedFlow(email: String?, list: MutableList<String>) = flow {
        mCollection.document(email?:"").update("users_blocked", list).await()
        emit(true)
    }
}