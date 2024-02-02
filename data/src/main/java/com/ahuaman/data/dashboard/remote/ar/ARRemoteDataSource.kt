package com.ahuaman.data.dashboard.remote.ar

import javax.inject.Inject

class ARRemoteDataSource  @Inject constructor(
    private val arService: ARService
) {

    suspend fun downloadARModel(category: String, modelName: String)
    = arService.downloadARModel(category, modelName).body()?.bytes()

}