package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.remote.DashboardRemoteDataSource
import com.yakulab.framework.network.performNetworkWithFlow
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

    suspend fun getSpecieByTaxonId(taxon_id: String) = performNetworkWithFlow(
        networkCall = { dashboardRemoteDataSource.getSpecieByTaxonId(taxon_id) }
    )

}