package com.ahuaman.feature_ar.presentation.AR

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakulab.usecases.ar.DownloadModelInstanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ARScreenViewModel @Inject constructor(
    private val downloadModelInstanceUseCase: DownloadModelInstanceUseCase
) : ViewModel() {

    private val _model:MutableStateFlow<StateARScreen> = MutableStateFlow(StateARScreen.Loading)
    val model: StateFlow<StateARScreen> = _model.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StateARScreen.Loading
    )

    init {
        downloadModelInstance("insect", "ant")
    }

    fun downloadModelInstance(category: String, modelName: String) = viewModelScope.launch {
        downloadModelInstanceUseCase
            .invoke(category, modelName)
            .onStart {
                _model.value = StateARScreen.Loading
            }
            .onEach {
                it?.let {
                    Timber.d("onEach-ARVIEWMODEL - $it")
                    _model.value = StateARScreen.Success(it)
                }
            }.catch {
                Timber.d("catch-ARVIEWMODEL - ${it.message}")
            }.launchIn(viewModelScope)
    }

}