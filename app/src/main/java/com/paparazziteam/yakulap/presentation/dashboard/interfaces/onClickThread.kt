package com.paparazziteam.yakulap.presentation.dashboard.interfaces

import com.yakulab.domain.dashboard.ChallengeCompleted

interface onClickThread {
    fun clickOnUpdateLike(item: ChallengeCompleted)
    fun clickedComentThread(item: ChallengeCompleted)
    fun clickedReportThread(item: ChallengeCompleted)
}