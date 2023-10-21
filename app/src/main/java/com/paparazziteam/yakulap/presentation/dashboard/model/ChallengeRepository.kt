package com.paparazziteam.yakulap.presentation.dashboard.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.yakulab.domain.dashboard.ChallengeCompleted

interface ChallengeRepository {
    fun createDocument(): DocumentReference?
    fun create(sustantivo: ChallengeCompleted): Task<Void?>?
    fun update(id_photo: String?, url: String?): Task<Void?>?
    fun getListChallengesOrderByTimeStamp(): Query?
    fun search(emailSuscriber: String?, challengeId: String?): Query?
}