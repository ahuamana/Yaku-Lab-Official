package com.ahuaman.data.dashboard.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginProvider @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun getIsLogin(): Boolean {
        return firebaseAuth.currentUser != null
    }

    //Local
    fun getEmail(): String {
        return firebaseAuth.currentUser?.email?: ""
    }

    fun loginEmail(email:String, pass:String) = flow {
       val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
        emit(result)
    }

    fun loginAnonymous() = flow {
        val result = firebaseAuth.signInAnonymously().await()
        emit(result)
    }

    fun signOut() {
        return firebaseAuth.signOut()
    }

    fun createUser(email:String,pass:String) = flow {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
        emit(result)
    }

}