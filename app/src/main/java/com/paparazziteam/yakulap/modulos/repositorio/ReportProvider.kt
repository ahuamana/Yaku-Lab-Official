package com.paparazziteam.yakulap.modulos.repositorio

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.paparazziteam.yakulap.modulos.dashboard.pojo.ReportPost

class ReportProvider {

    var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Reports")

    fun create(post: ReportPost): Task<Void?>? {
        return mCollection.document().set(post)
    }
}