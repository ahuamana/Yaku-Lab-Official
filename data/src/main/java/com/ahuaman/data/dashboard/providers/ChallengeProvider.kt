package com.ahuaman.data.dashboard.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.yakulab.domain.dashboard.ChallengeCompleted
import javax.inject.Inject

class ChallengeProvider @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    val mCollection: CollectionReference = firebaseFirestore.collection("LaboratorioDigital")

    fun createDocument(): DocumentReference? {
        return mCollection.document()
    }

    fun create(sustantivo: ChallengeCompleted): Task<Void?>? {
        return mCollection.document(sustantivo.id?:"").set(sustantivo)
    }

    fun update(id_photo: String?, url: String?): Task<Void?>? {
        return mCollection.document(id_photo!!).update("url", url)
    }

    fun getListChallengesOrderByTimeStamp(): Query? {
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun search(emailSuscriber: String?, challengeId: String?): Query? {
        return mCollection
            .whereEqualTo("author_email", emailSuscriber)
            //.whereEqualTo("name", nameSustantivo)
            .whereEqualTo("challenge_id", challengeId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }
}