package com.paparazziteam.yakulap.modulos.login.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginProvider {

    companion object{
        private lateinit var mAuth: FirebaseAuth
    }

    init {
        mAuth = FirebaseAuth.getInstance()
    }

    fun getIsLogin(): Boolean
    {
        return mAuth.currentUser != null
    }

    fun getEmail(): String?
    {
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

}