package com.ahuaman.data.dashboard.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yakulab.domain.dashboard.Comment
import javax.inject.Inject

class CommentProvider  @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    val mCollection: CollectionReference = firebaseFirestore.collection("Comment")

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