package com.paparazziteam.yakulap.presentation.login.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yakulab.usecases.firebase.login.GetLoginWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val getLoginWithEmailUseCase: GetLoginWithEmailUseCase
) : ViewModel() {

    private val _message = MutableLiveData<String>()
    private val _isLoginEmail = MutableStateFlow(false)
    val isLoginEmail = _isLoginEmail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000,1),
        initialValue = false
    )
    private val _isLoginAnonymous = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    fun getIsLoading(): LiveData<Boolean> = _isLoading
    fun showMessage(): LiveData<String> = _message
    fun getIsLoginAnonymous(): LiveData<Boolean> = _isLoginAnonymous


    fun loginWithEmail(email: String?, pass: String?) = viewModelScope.launch {
        getLoginWithEmailUseCase
            .invoke(email?:"", pass?:"")
            .onStart {
                _isLoading.value = true
            }.onEach {
                it.user?.let { user->
                    firebaseCrashlytics.setUserId(user.email?:"")
                }
                _message.value = "Bienvenido"
                _isLoginEmail.value = true
                _isLoading.value = false
            }.catch {
                _message.value = "Usuario y/o contraseña incorrectos"
                _isLoginEmail.value = false
                _isLoading.value = false
            }.launchIn(viewModelScope)
    }

}
