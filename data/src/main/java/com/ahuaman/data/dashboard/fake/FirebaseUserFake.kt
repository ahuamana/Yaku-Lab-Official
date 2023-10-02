package com.ahuaman.data.dashboard.fake

import com.yakulab.domain.profile.MedalsDomain
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseUserFake @Inject constructor() {


    fun getMedals () = flow {
        emit(MedalsFakeData.data)
    }

    fun getCertifications() = flow {
        emit(CertificationsFake.data)
    }
}