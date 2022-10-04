package com.paparazziteam.yakulap.modulos.dashboard.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.paparazziteam.yakulap.modulos.dashboard.di.DashboardModule
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Comment
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    @DashboardModule.CommentCollection private val mCollection: CollectionReference
): CommentRepository {
    //var mCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Comment")
    override fun create(comment: Comment): Task<Void?>? {
        val document = mCollection.document()
        comment.id =document.id
        return document.set(comment)
    }

    override fun getCommentsByIdPhoto(idphoto: String?): Query? {
        return mCollection
            .whereEqualTo("id_photo", idphoto)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }
}