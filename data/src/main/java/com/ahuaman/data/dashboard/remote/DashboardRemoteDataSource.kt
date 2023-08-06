package com.ahuaman.data.dashboard.remote

import com.yakulab.framework.network.BaseDataSource
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


    suspend fun getSpecieByTaxonId(taxon_id: String) = getResultWithForce(
        call = { dashboardService.getSpecieByTaxonId(taxon_id) },
        forceError = false
    )


}