package com.ahuaman.data.dashboard.remote

import com.paparazziteam.yakulab.binding.Constants.PATH_OBSERVATIONS
import com.yakulab.domain.dashboard.ObservationItemResponse
import com.yakulab.domain.dashboard.ObservationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DashboardService {

    //GET observations?lat=37.7749&lng=-122.4194&order=desc&per_page=20
    // get observations by location
    @GET("$PATH_OBSERVATIONS")
    suspend fun getSpeciesByLocation(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("order") order: String = "desc",
        @Query("per_page") per_page: Int = 20
    ): Response<ObservationResponse>

    //GET getSpecieById
    //https://api.inaturalist.org/v1/observations/176636852

    @GET ("$PATH_OBSERVATIONS/{taxon_id}")
    suspend fun getSpecieByTaxonId(
        @Path("taxon_id") taxon_id: String,
    ): Response<ObservationItemResponse>


}