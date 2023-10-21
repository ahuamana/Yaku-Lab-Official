package com.paparazziteam.yakulap.presentation.dashboard.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakulab.usecases.inaturalist.ActionFile
import com.yakulab.usecases.inaturalist.DownloadFileFromFirebase
import com.yakulab.usecases.inaturalist.DownloadFileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SlideImageViewModel @Inject constructor(
    private val downloadFileFromFirebase: DownloadFileFromFirebase
) : ViewModel() {

    private val _eventsDownloadFile = MutableStateFlow<DownloadFileResult>(DownloadFileResult.Idle)
    val eventsDownloadFile = _eventsDownloadFile.asStateFlow()

    fun downloadFile(url: String, action: ActionFile) = viewModelScope.launch {
        downloadFileFromFirebase
            .invoke(url)
            .onStart {
                _eventsDownloadFile.value = DownloadFileResult.ShowLoading
            }.onEach {
                when (action) {
                    ActionFile.SHARE -> {
                        it?.let { input ->
                            val bitmap = convertInputStreamToBitmap(input)
                            _eventsDownloadFile.value = DownloadFileResult.SuccessShare(bitmap)
                        }
                    }
                    ActionFile.DOWNLOAD -> {
                        it?.let { input ->
                            val bitmap = convertInputStreamToBitmap(input)
                            _eventsDownloadFile.value = DownloadFileResult.SuccessDownload(bitmap)
                        }
                    }
                }
            }.catch {
                _eventsDownloadFile.value = DownloadFileResult.Failure("Error")
            }.onCompletion {
                _eventsDownloadFile.value = DownloadFileResult.HideLoading
            }.launchIn(viewModelScope)
    }

    suspend fun convertInputStreamToBitmap(inputStream: InputStream): Bitmap {
        val defered = viewModelScope.async {
            BitmapFactory.decodeStream(inputStream)
        }
        return defered.await()
    }
}