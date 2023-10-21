package com.ahuaman.data.dashboard.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.yakulab.domain.login.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRemote @Inject constructor() {
    val mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Users")

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        FirebaseFirestore.getInstance().firestoreSettings = settings
    }

    fun create(user: User): Task<Void> {
        return mCollection.document(user.email?:"").set(user)
    }

    fun searchUserByEmail(email: String?): Task<DocumentSnapshot> {
        return mCollection.document(email?:"").get()
    }

    suspend fun getUser(email: String?): Flow<User> {
        return mCollection.document(email?:"").get().await().toObject(User::class.java)?.let { user ->
            kotlinx.coroutines.flow.flow {
                emit(user)
            }
        } ?: kotlinx.coroutines.flow.flow {
            emit(User())
        }
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