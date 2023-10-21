package com.yakulab.feature.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.yakulab.binding.utils.text.capitalizeWords
import com.yakulab.domain.profile.MedalsDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileParentViewModel  @Inject constructor(
    private val getEmailLoggedUseCase: com.yakulab.usecases.firebase.getEmailLoggedUseCase,
    private val getUserInfoUseCase: com.yakulab.usecases.firebase.getUserInfoUseCase,
    private val getMedalsUseCase: com.yakulab.usecases.yakulab.GetMedalsUseCase,
    private val getCertificationsUseCase: com.yakulab.usecases.yakulab.GetCertificationsUseCase,
    private val logoutUseCase: com.yakulab.usecases.yakulab.LogoutUseCase
) : ViewModel() {


    init {
        val email = getEmailLoggedUseCase.invoke()
        getUserInfo(email)
    }

    private val _user: MutableStateFlow<String> = MutableStateFlow("")
    val user = _user

    private val _medals: MutableStateFlow<List<MedalsDomain>> = MutableStateFlow(emptyList())
    val medals:StateFlow<List<MedalsDomain>> = _medals.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000, 1),
        emptyList()
    )

    private val _certifications: MutableStateFlow<List<com.yakulab.domain.profile.CertificationsDomain>> = MutableStateFlow(emptyList())
    val certifications:StateFlow<List<com.yakulab.domain.profile.CertificationsDomain>> = _certifications.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000, 1),
        emptyList()
    )

    init {
        getMedals()
        getCertifications()
    }

    fun logout() = viewModelScope.launch {

        logoutUseCase.invoke()
    }

    private fun getUserInfo(email: String) = viewModelScope.launch {
        getUserInfoUseCase
            .invoke(email)
            .onEach {
                if(it.email == null) {
                    Timber.d("User is null: ${it.email}")
                    return@onEach
                }
                //first_name, last_name
                val name = it.nombres?.capitalizeWords()
                val lastName = it.apellidos?.capitalizeWords()
                _user.value = "${name}, $lastName"
            }.catch {
                Timber.e("Error: ${it.message}")
            }.launchIn(viewModelScope)
    }

    private fun getMedals() = viewModelScope.launch {
        getMedalsUseCase
            .invoke()
            .onEach {
                _medals.value = it
            }.catch {
                Timber.e("Error: ${it.message}")
            }.launchIn(viewModelScope)
    }

    private fun getCertifications() = viewModelScope.launch {
        getCertificationsUseCase
            .invoke()
            .onEach {
                _certifications.value = it
            }.catch {
                Timber.e("Error: ${it.message}")
            }.launchIn(viewModelScope)
    }

}