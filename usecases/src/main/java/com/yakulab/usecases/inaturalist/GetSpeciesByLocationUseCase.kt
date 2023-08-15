package com.yakulab.usecases.inaturalist

import com.ahuaman.data.dashboard.repository.DashboardRepository
import com.yakulab.domain.dashboard.ObservationEntity
import com.yakulab.domain.dashboard.toObservationEntityList
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
            it.results.toObservationEntityList().distinctBy {
                it.taxon?.name
            }
    }
}

sealed class SpeciesByLocationResult {
    object ShowLoading : SpeciesByLocationResult()
    object HideLoading : SpeciesByLocationResult()
    data class Success(val species: List<ObservationEntity>) : SpeciesByLocationResult()
    object Empty : SpeciesByLocationResult()
    data class Error(val message: String) : SpeciesByLocationResult()
}