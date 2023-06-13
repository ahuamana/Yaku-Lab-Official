package com.paparazziteam.yakulap.usecases

import com.paparazziteam.yakulap.data.dashboard.repository.DashboardRepository
import com.paparazziteam.yakulap.domain.dashboard.Observation
import com.paparazziteam.yakulap.domain.dashboard.ObservationEntity
import com.paparazziteam.yakulap.domain.dashboard.ObservationResponse
import com.paparazziteam.yakulap.domain.dashboard.toObservationEntityList
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSpeciesByLocationUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lng: Double,
        order: String = "desc",
        per_page: Int = 20) = dashboardRepository.getSpeciesByLocation(lat, lng, order, per_page).map {
            it.results.toObservationEntityList()
    }
}

sealed class SpeciesByLocationResult {
    object ShowLoading : SpeciesByLocationResult()
    object HideLoading : SpeciesByLocationResult()
    data class Success(val species: List<ObservationEntity>) : SpeciesByLocationResult()
    object Empty : SpeciesByLocationResult()
    data class Error(val message: String) : SpeciesByLocationResult()
}