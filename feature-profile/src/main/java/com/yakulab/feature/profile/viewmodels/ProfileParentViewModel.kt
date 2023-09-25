package com.yakulab.feature.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakulab.usecases.inaturalist.firebase.getEmailLoggedUseCase
import com.yakulab.usecases.inaturalist.firebase.getUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileParentViewModel  @Inject constructor(
    private val getUserInfoUseCase: getUserInfoUseCase,
    private val getEmailLoggedUseCase: getEmailLoggedUseCase
) : ViewModel() {

    init {
        val email = getEmailLoggedUseCase.invoke()
        getUserInfo(email)
    }

    private val _user: MutableStateFlow<String> = MutableStateFlow("")
    val user = _user

    fun getUserInfo(email: String) = viewModelScope.launch {
        getUserInfoUseCase
            .invoke(email)
            .onEach {
                if(it.email == null) {
                    Timber.d("User is null: ${it.email}")
                    return@onEach
                }
                //first_name, last_name
                _user.value = "${it.nombres}, ${it.apellidos}"
            }.catch {
                Timber.e("Error: ${it.message}")
            }.launchIn(viewModelScope)
    }
}