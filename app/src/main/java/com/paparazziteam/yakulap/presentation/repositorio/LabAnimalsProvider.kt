package com.paparazziteam.yakulap.presentation.repositorio

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.yakulab.domain.dashboard.TypeGroup

class LabAnimalsProvider {

    var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("ChallengesAnimales")

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        FirebaseFirestore.getInstance().firestoreSettings = settings
    }


    fun geDataProvider(type: String?): Task<DocumentSnapshot> {
        when(type){
            TypeGroup.FRUITS.value->{
                return mCollection.document("FRUITS").get()
            }

            TypeGroup.PLANTS.value->{
                return mCollection.document("PLANTS").get()
            }

            TypeGroup.ANIMALS.value->{
                return mCollection.document("Data").get()
            }

            else->{
                return mCollection.document("Data").get()
            }
        }

    }
}