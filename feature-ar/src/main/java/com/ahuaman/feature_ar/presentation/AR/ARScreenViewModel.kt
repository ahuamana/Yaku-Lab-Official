package com.ahuaman.feature_ar.presentation.AR

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakulab.usecases.ar.DownloadModelInstanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.delay
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
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class ARScreenViewModel @Inject constructor(
    private val downloadModelInstanceUseCase: DownloadModelInstanceUseCase
) : ViewModel() {

    private val _model:MutableStateFlow<BaseState> = MutableStateFlow(BaseState())
    val model: StateFlow<BaseState> = _model.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BaseState()
    )


    init {
        //downloadModelInstance("insect", "ant")
    }

    fun downloadModelInstance(category: String, modelName: String) = viewModelScope.launch {
        downloadModelInstanceUseCase
            .invoke(category, modelName)
            .onStart {
                _model.value = BaseState(isLoading = true)
            }
            .onEach {
                it?.let {
                    Timber.d("onEach-ARVIEWMODEL - $it")
                    _model.value = BaseState(isSuccess = listOf(StateARScreen.SuccessArray(it)))
                }
            }.catch {
                Timber.d("catch-ARVIEWMODEL - ${it.message}")
            }.launchIn(viewModelScope)
    }

    fun getModel(modelLoader:ModelLoader, arModel:String) = viewModelScope.launch {
        modelLoader.loadModelInstanceAsync(arModel){
            _model.value = BaseState(
                isSuccess = listOf(
                    StateARScreen.SuccessModel(it))
                )
        }
    }

}