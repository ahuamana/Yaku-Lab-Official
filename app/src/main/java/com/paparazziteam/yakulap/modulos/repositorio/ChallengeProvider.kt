package com.paparazziteam.yakulap.modulos.repositorio

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.paparazziteam.yakulap.modulos.dashboard.pojo.ChallengeCompleted

class ChallengeProvider {

    var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("LaboratorioDigital")

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        FirebaseFirestore.getInstance().firestoreSettings = settings
    }

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