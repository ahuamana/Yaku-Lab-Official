package com.paparazziteam.yakulap.presentation.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahuaman.data.dashboard.providers.UserProvider
import com.google.firebase.auth.FirebaseUser
import com.yakulab.domain.login.User
import com.yakulab.usecases.firebase.login.RegisterUserUseCase
import com.yakulab.usecases.firebase.user.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRegisterUser @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val createUserUseCase: CreateUserUseCase
):ViewModel() {

    var user: FirebaseUser? = null
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

    fun saveFirebaseUser(usernew: User) = viewModelScope.launch{
        createUserUseCase
            .invoke(usernew)
            .onStart {
                _isLoading.value = true
            }.onEach {
                _isSavedFirebase.value = it
            }.catch {
                _message.value = "Ah ocurrido un error al intentar crear un usuario nuevo"
                _isLoading.value = false
            }.launchIn(viewModelScope)
    }
}