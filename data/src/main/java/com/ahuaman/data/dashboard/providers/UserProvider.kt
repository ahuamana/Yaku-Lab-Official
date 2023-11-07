package com.ahuaman.data.dashboard.providers

import com.ahuaman.data.dashboard.di.DashboardModule
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.yakulab.domain.login.User
import javax.inject.Inject

class UserProvider @Inject constructor(
    @DashboardModule.UsersCollection private val mCollection:CollectionReference
) {



    fun create(user: User): Task<Void> {
        return mCollection.document(user.email?:"").set(user)
    }

    fun searchUserByEmail(email: String?): Task<DocumentSnapshot> {
        return mCollection.document(email?:"").get()
    }

    fun updatePoints(email: String?, points: Int): Task<Void?>? {
        return mCollection.document(email?:"").update("points", points)
    }

    fun updatePostBlocked(email: String?, list: MutableList<String>): Task<Void?>? {
        return mCollection.document(email?:"").update("post_blocked", list)
    }

    fun updateUsersBlocked(email: String?, list: MutableList<String>): Task<Void?>? {
        return mCollection.document(email?:"").update("users_blocked", list)
    }
}