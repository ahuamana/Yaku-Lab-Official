package com.paparazziteam.yakulap.modulos.dashboard.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Comment

interface CommentRepository {
    fun create(comment: Comment): Task<Void?>?
    fun getCommentsByIdPhoto(idphoto: String?): Query?
}