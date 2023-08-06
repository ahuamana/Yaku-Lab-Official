package com.paparazziteam.yakulap.presentation.repositorio

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yakulab.domain.dashboard.Reaccion
import javax.inject.Inject

class ReaccionProvider @Inject constructor(){

    var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Actions")

    fun create(reaccion: Reaccion): Task<Void?>? {
        return reaccion.id_email?.let { mCollection.document(it).set(reaccion) }
    }

    fun getUserLike(email: String?, idPhoto: String?): Query? {
        return mCollection
            .whereEqualTo("email", email)
            .whereEqualTo("id", idPhoto)
    }

    fun updateStatus(idEmail: String, status: Boolean): Task<Void?>? {
        val reaccion: MutableMap<String, Any> = HashMap()
        reaccion["status"] = status
        return mCollection.document(idEmail).update(reaccion)
    }
}