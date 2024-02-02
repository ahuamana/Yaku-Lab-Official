package com.ahuaman.data.dashboard.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.yakulab.domain.dashboard.TypeGroup
import javax.inject.Inject

class LabAnimalsProvider @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    val mCollection: CollectionReference = firebaseFirestore.collection("ChallengesAnimales")

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

