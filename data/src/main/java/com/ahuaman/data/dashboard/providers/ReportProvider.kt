package com.ahuaman.data.dashboard.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yakulab.domain.dashboard.Report
import javax.inject.Inject

class ReportProvider @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    val mCollection: CollectionReference =firebaseFirestore.collection("Reports")

    fun create(post: Report): Task<Void?>? {
        return mCollection.document().set(post)
    }
}