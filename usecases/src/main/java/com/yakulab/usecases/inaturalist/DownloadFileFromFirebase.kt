package com.yakulab.usecases.inaturalist

import android.graphics.Bitmap
import com.ahuaman.data.dashboard.remote.FirebaseImageRemoteDataSource
import com.ahuaman.data.dashboard.repository.DashboardRepository
import javax.inject.Inject

class DownloadFileFromFirebase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(url: String) = repository.downloadFile(url)
}


sealed class DownloadFileResult {
    object Idle : DownloadFileResult()
    object HideLoading : DownloadFileResult()
    object ShowLoading : DownloadFileResult()
    data class SuccessShare(val response: Bitmap) : DownloadFileResult()
    data class SuccessDownload(val response: Bitmap) : DownloadFileResult()
    data class Failure(val error: String) : DownloadFileResult()
}

enum class ActionFile {
    SHARE, DOWNLOAD
}