package com.paparazziteam.yakulap.presentation.dashboard.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.paparazziteam.yakulap.presentation.dashboard.di.DashboardModule
import com.yakulab.domain.dashboard.ChallengeCompleted
import javax.inject.Inject


class ChallengeRepositoryImpl @Inject constructor(
    @DashboardModule.LaboratoryCollection private val mCollection: CollectionReference
): ChallengeRepository {
    /*var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("LaboratorioDigital")
    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        authFirestore.firestoreSettings = settings
    }*/
    override fun create(sustantivo: ChallengeCompleted): Task<Void?>? {
        return mCollection.document(sustantivo.id?:"").set(sustantivo)
    }

    override fun update(id_photo: String?, url: String?): Task<Void?>? {
        return mCollection.document(id_photo!!).update("url", url)
    }

    override fun getListChallengesOrderByTimeStamp(): Query? {
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING)
    }

    override fun search(emailSuscriber: String?, challengeId: String?): Query? {
        return mCollection
            .whereEqualTo("author_email", emailSuscriber)
            //.whereEqualTo("name", nameSustantivo)
            .whereEqualTo("challenge_id", challengeId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    override fun createDocument(): DocumentReference? {
        return mCollection.document()
    }
}