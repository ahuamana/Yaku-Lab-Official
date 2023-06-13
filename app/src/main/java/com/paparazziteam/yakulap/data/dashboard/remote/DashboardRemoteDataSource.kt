package com.paparazziteam.yakulap.data.dashboard.remote

import com.paparazziteam.yakulap.helper.network.BaseDataSource
import javax.inject.Inject

class DashboardRemoteDataSource @Inject constructor(
    private val dashboardService: DashboardService
) : BaseDataSource() {

    suspend fun getSpeciesByLocation(lat: Double,
                                lng: Double,
                                order: String,
                                per_page: Int) = getResultWithForce(
        call = { dashboardService.getSpeciesByLocation(lat, lng, order, per_page) },
        forceError = false
    )




}