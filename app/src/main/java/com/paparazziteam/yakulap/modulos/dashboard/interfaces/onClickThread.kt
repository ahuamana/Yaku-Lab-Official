package com.paparazziteam.yakulap.modulos.dashboard.interfaces

import com.paparazziteam.yakulap.modulos.dashboard.pojo.ChallengeCompleted

interface onClickThread {
    fun clickOnUpdateLike(item:ChallengeCompleted)
    fun clickedComentThread(item:ChallengeCompleted)
    fun clickedReportThread(item:ChallengeCompleted)
}