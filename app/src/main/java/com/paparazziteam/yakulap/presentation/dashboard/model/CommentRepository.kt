package com.paparazziteam.yakulap.presentation.dashboard.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.yakulab.domain.dashboard.Comment

interface CommentRepository {
    fun create(comment: Comment): Task<Void?>?
    fun getCommentsByIdPhoto(idphoto: String?): Query?
}