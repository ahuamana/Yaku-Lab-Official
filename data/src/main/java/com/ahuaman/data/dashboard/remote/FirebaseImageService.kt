package com.ahuaman.data.dashboard.remote

import com.yakulab.framework.network.BaseDataSource
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import javax.inject.Inject

interface FirebaseImageService{
    @GET
    @Streaming
    suspend fun downloadFile(@Url url:String):Response<ResponseBody>

}