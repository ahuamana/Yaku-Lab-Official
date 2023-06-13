package com.paparazziteam.yakulap.data.dashboard.repository

import com.paparazziteam.yakulap.data.dashboard.remote.DashboardRemoteDataSource
import com.paparazziteam.yakulap.helper.network.performNetworkWithFlow
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val dashboardRemoteDataSource: DashboardRemoteDataSource
){
    suspend fun getSpeciesByLocation(
        lat: Double,
        lng: Double,
        order: String,
        per_page: Int) = performNetworkWithFlow(
        networkCall = { dashboardRemoteDataSource.getSpeciesByLocation(lat, lng, order, per_page) }
    )

}