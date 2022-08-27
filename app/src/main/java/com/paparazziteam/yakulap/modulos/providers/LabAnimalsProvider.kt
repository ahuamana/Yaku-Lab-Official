package com.paparazziteam.yakulap.modulos.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted

class LabAnimalsProvider {

    var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("ChallengesAnimales")

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        FirebaseFirestore.getInstance().firestoreSettings = settings
    }


    fun geDataProvider(): Task<DocumentSnapshot> {
        return mCollection.document("Data").get()
    }
}