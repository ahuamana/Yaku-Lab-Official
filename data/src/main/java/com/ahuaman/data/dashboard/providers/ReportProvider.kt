package com.ahuaman.data.dashboard.providers

import com.ahuaman.data.dashboard.di.ReportModule
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yakulab.domain.dashboard.Report
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReportProvider @Inject constructor(
   @ReportModule.ReportsCollection private val mCollection: CollectionReference
) {

    fun create(post: Report)= flow {
        mCollection.document().set(post)
        emit(true)
    }
}