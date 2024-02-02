package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.remote.ar.ARRemoteDataSource
import com.yakulab.framework.network.performNetworkWithFlow
import javax.inject.Inject

class ARRepository @Inject constructor(
    private val arRemoteDataSource: ARRemoteDataSource
) {

    suspend fun downloadARModel(category: String, modelName: String) = performNetworkWithFlow {
        arRemoteDataSource.downloadARModel(category, modelName)
    }

}
