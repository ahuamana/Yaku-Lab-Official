package com.ahuaman.data.dashboard.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginProvider @Inject constructor() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getIsLogin(): Boolean {
        return mAuth.currentUser != null
    }

    //Local
    fun getEmail(): String {
        return mAuth.currentUser?.email?: ""
    }

    fun loginEmail(email:String, pass:String) = flow {
       val result = mAuth.signInWithEmailAndPassword(email, pass).await()
        emit(result)
    }

    fun loginAnonymous() = flow {
        val result = mAuth.signInAnonymously().await()
        emit(result)
    }

    fun signOut() {
        return mAuth.signOut()
    }

    fun createUser(email:String,pass:String) = flow {
        val result = mAuth.createUserWithEmailAndPassword(email, pass).await()
        emit(result)
    }

}