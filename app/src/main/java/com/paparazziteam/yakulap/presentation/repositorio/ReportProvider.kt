package com.paparazziteam.yakulap.presentation.repositorio

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yakulab.domain.dashboard.Report
import javax.inject.Inject

class ReportProvider @Inject constructor() {

    var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Reports")

    fun create(post: Report): Task<Void?>? {
        return mCollection.document().set(post)
    }
}