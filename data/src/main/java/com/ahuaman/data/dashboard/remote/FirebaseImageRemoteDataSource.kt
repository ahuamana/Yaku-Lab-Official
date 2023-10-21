package com.ahuaman.data.dashboard.remote

import com.yakulab.framework.network.BaseDataSource
import javax.inject.Inject

class FirebaseImageRemoteDataSource @Inject constructor(
    private val firebaseImageService: FirebaseImageService
) : BaseDataSource() {

        suspend fun downloadFile(url: String) =  firebaseImageService.downloadFile(url).body()?.byteStream()
}