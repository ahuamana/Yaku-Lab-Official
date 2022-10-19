package com.paparazziteam.yakulap.modulos.repositorio

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Comment

class CommentProvider {

    var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Comment")

    fun create(comment: Comment): Task<Void?>? {
        val document = mCollection.document()
        comment.id =document.id
        return document.set(comment)
    }

    fun getCommentsByIdPhoto(idphoto: String?): Query? {
        return mCollection
            .whereEqualTo("id_photo", idphoto)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }
}