package com.ahuaman.data.dashboard.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
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

    fun loginEmail(email:String, pass:String): Task<AuthResult> {
       return mAuth.signInWithEmailAndPassword(email, pass)
    }

    fun loginAnonymous(): Task<AuthResult> {
        return  mAuth.signInAnonymously()
    }

    fun signOut() {
        return mAuth.signOut()
    }

    fun createUser(email:String,pass:String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, pass)
    }

}