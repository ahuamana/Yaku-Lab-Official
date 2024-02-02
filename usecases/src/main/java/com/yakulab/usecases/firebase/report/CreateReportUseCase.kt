package com.yakulab.usecases.firebase.report

import com.ahuaman.data.dashboard.repository.ReportRepository
import com.yakulab.domain.dashboard.Report
import javax.inject.Inject

class CreateReportUseCase @Inject constructor(
    private val repository: ReportRepository
) {

        fun invoke(report: Report) = repository.createReport(report)
}