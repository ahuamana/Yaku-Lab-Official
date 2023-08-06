package com.yakulab.usecases.inaturalist

import com.ahuaman.data.dashboard.repository.DashboardRepository
import javax.inject.Inject

class GetSpeciesByTaxonIdUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
){
    suspend operator fun invoke(taxon_id: String) = dashboardRepository.getSpecieByTaxonId(taxon_id)
}