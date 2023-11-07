package com.paparazziteam.yakulap.presentation.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.yakulab.domain.login.User
import com.yakulab.usecases.firebase.login.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRegisterUser @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
):ViewModel() {

    var user: FirebaseUser? = null
    var mUserProvider = com.ahuaman.data.dashboard.providers.UserProvider()
    private val _message = MutableLiveData<String>()
    private val _user = MutableLiveData<FirebaseUser>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isSavedFirebase = MutableLiveData<Boolean>()

    fun getIsSavedFirebase(): LiveData<Boolean> =  _isSavedFirebase
    fun getIsLoading(): LiveData<Boolean> = _isLoading
    fun showMessage(): LiveData<String>  = _message
    fun getUser(): LiveData<FirebaseUser>  = _user

    fun createUser(email: String, pass: String) = viewModelScope.launch {

        registerUserUseCase
            .invoke(email, pass)
            .onStart {
                _isLoading.value = true
            }.onEach { auth->
                auth.user?.let {
                    user = it
                    _user.value = user
                }
            }.catch {
                _message.value = "Ah ocurrido un error al intentar crear un usuario nuevo"
                _isLoading.value = false
            }.launchIn(viewModelScope)
    }

    fun saveFirebaseUser(usernew: User) {
        try {
            mUserProvider.create(usernew).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoading.setValue(false)
                    _isSavedFirebase.setValue(true)
                } else {
                    _isLoading.setValue(false)
                    _isSavedFirebase.setValue(false)
                }
            }.addOnFailureListener{ e ->
                _isLoading.setValue(false)
                _message.setValue(e.message)
                _isSavedFirebase.setValue(false)
            }

        } catch (e: java.lang.Exception) {
            _isLoading.setValue(false)
            _message.setValue(e.message)
            _isSavedFirebase.setValue(false)
        }
    }
}