package com.paparazziteam.yakulap.data.dashboard.remote

import com.paparazziteam.yakulap.domain.dashboard.ObservationResponse
import com.paparazziteam.yakulap.helper.Constants.PATH_OBSERVATIONS
import retrofit2.Response
import retrofit2.http.GET
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



}