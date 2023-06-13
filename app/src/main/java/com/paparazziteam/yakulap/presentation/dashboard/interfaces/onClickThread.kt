package com.paparazziteam.yakulap.presentation.dashboard.interfaces

import com.paparazziteam.yakulap.presentation.dashboard.pojo.ChallengeCompleted

interface onClickThread {
    fun clickOnUpdateLike(item:ChallengeCompleted)
    fun clickedComentThread(item:ChallengeCompleted)
    fun clickedReportThread(item:ChallengeCompleted)
}