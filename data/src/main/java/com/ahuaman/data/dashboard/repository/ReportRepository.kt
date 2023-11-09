package com.ahuaman.data.dashboard.repository

import com.ahuaman.data.dashboard.providers.ReportProvider
import com.yakulab.domain.dashboard.Report
import javax.inject.Inject

class ReportRepository @Inject constructor(
    private val reportRepository: ReportProvider
){

    fun createReport(report: Report) = reportRepository.create(report)
}