package com.paparazziteam.yakulap.modulos.dashboard.pojo

import com.paparazziteam.yakulap.helper.application.MyPreferences

data class ReportPost (
    var idPostReported: String? = null,
    var idChallengeReported: String? = null,
    var typeReport:String?=null,
    var typeReportedPost:String?=null,
    var idCommentReported:String?=null,
    var idPhotoReported:String?=null,
    var reportedComentario:String?=null,
    var emailWhoReport: String? = MyPreferences().email_login,
    var lastNameWhoReport: String? = MyPreferences().lastName,
    var firstNameWhoReport: String? = MyPreferences().firstName,
)

enum class TypeReported(val value:String) {
    POST("POST"),
    COMMENT("COMMENT")
}

enum class TypeReportedPost(val value:String) {
    NAKED("NAKED"),
    VIOLENCE("VIOLENCE"),
    TERRORISM("TERRORISM"),
    HARASSMENT("HARASSMENT"),
    SUICIDE("SUICIDE"),
    OTHER("OTHER")
}


