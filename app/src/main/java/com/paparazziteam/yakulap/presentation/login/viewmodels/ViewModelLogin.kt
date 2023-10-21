package com.paparazziteam.yakulap.presentation.login.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yakulab.usecases.yakulab.LoginWithEmailUseCase
import com.yakulab.usecases.yakulab.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor() : ViewModel() {

    private var mLoginProvider = com.ahuaman.data.dashboard.providers.LoginProvider()
    private val _message = MutableLiveData<String>()
    private val _isLoginEmail = MutableLiveData<Boolean>()
    private val _isLoginAnonymous = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    fun getIsLoading(): LiveData<Boolean> = _isLoading
    fun showMessage(): LiveData<String> = _message
    fun getIsLoginEmail(): LiveData<Boolean> = _isLoginEmail
    fun getIsLoginAnonymous(): LiveData<Boolean> = _isLoginAnonymous


    fun isAlreadyLogging(): LiveData<String?> {
        if(mLoginProvider.getIsLogin()) {
            _message.setValue("Ya tienes un Inicio de Session")
        }else _message.setValue("No tienes un Inicio de Session")
        return _message
    }

    fun loginWithEmail(email: String?, pass: String?) {
        _isLoading.setValue(true)
        try {
            mLoginProvider.loginEmail(email?:"", pass?:"").addOnCompleteListener {
                Timber.d("LoginWithEmail: ${it.isSuccessful}")
                if (it.isSuccessful) {
                    _message.value = "Bienvenido"
                    _isLoginEmail.value = true
                } else {
                    Timber.e("Error: ${it.exception?.message}")
                    _message.value = "Usuario y/o contraseña incorrectos"
                    _isLoginEmail.value = false
                }
                _isLoading.value =false
            }

        } catch (e: Exception) {
            _message.setValue(e.message)
        }
    }


    fun loginAnonymous() {
        _isLoading.setValue(true)
        try {
            mLoginProvider.loginAnonymous().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _message.setValue("Bienvenido anónimo")
                        try {
                            Thread.sleep(2000)
                        } catch (e: java.lang.Exception) {
                            Log.e("TAG", "Error esperando")
                        }
                        _isLoginAnonymous.value = true
                        _isLoading.value = false
                    } else {
                        _message.setValue("No es posible ingresar. Porfavor contacta con soporte")
                        _isLoginAnonymous.setValue(false)
                        _isLoading.setValue(false)
                    }
                }.addOnFailureListener{
                    e -> _message.setValue("" + e.message)
                }
        } catch (e: java.lang.Exception) {
            Log.e("VM_LOGIN", "Error:" + e.message)
        }
    }

}
