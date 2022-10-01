package com.paparazziteam.yakulap.modulos.dashboard.interfaces

import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted

interface onClickThread {
    fun clickOnUpdateLike(item:MoldeChallengeCompleted)
    fun clickedComentThread(item:MoldeChallengeCompleted)
    fun clickedReportThread(item:MoldeChallengeCompleted)
}