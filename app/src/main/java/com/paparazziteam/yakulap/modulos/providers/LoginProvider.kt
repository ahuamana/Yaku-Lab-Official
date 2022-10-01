package com.paparazziteam.yakulap.modulos.login.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginProvider @Inject constructor() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getIsLogin(): Boolean {
        return mAuth.currentUser != null
    }

    //Local
    fun getEmail(): String? {
        return if(mAuth.currentUser != null) {
            mAuth.currentUser!!.email
        }else {
            ""
        }
    }

    fun loginEmail(email:String, pass:String): Task<AuthResult> {
       return mAuth.signInWithEmailAndPassword(email, pass)
    }

    fun loginAnonimously(): Task<AuthResult> {
        return  mAuth.signInAnonymously()
    }

    fun signout() {
        return mAuth.signOut()
    }

    fun createUser(email:String,pass:String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, pass)
    }

}