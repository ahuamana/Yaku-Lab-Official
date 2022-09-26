package com.paparazziteam.yakulap.modulos.dashboard.pojo

import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.toJson
import kotlinx.serialization.SerialName

data class ReportPost (
    var idPostReported: String? = null,
    var idChallengeReported: String? = null,
    var typeReport:String?=null,
    var idCommentReported:String?=null,
    var idPhotoReported:String?=null,
    var reportedComentario:String?=null,
    var emailWhoReport: String? = MyPreferences().email_login,
    var lastNameWhoReport: String? = MyPreferences().lastName,
    var firstNameWhoReport: String? = MyPreferences().firstName,
)

enum class TypeReport(val value:String) {
    THREAD("THREAD"),
    COMMENT("COMMENT")
}