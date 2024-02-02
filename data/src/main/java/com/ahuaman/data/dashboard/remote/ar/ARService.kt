package com.ahuaman.data.dashboard.remote.ar

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ARService {

    //https://ahuamana.github.io/models-ar/biology/plant-cell.glb

    @Streaming
    @GET("models-ar/{category}/{modelName}.glb")
    suspend fun downloadARModel(
        @Path("category") category: String,
        @Path("modelName") modelName: String
    ): Response<ResponseBody>
}