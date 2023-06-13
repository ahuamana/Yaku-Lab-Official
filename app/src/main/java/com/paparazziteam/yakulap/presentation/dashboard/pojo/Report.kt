package com.paparazziteam.yakulap.presentation.dashboard.pojo

import com.paparazziteam.yakulap.helper.application.MyPreferences

data class Report (
    var idPostReported: String? = null,
    var idChallengeReported: String? = null,
    var typeReport:String?=null,
    var typeReportedPost:String?=null,
    var idCommentReported:String?=null,
    var idPhotoReported:String?=null,
    var reportedComentario:String?=null,
    var userReported:String?=null,
    var emailWhoReport: String? = MyPreferences().email,
    var lastNameWhoReport: String? = MyPreferences().lastName,
    var firstNameWhoReport: String? = MyPreferences().firstName,
    var datetimeUnixTime: Long? = null,
    var datetime: String? = null,
)

enum class TypeReported(val value:String) {
    POST("POST"),
    COMMENT("COMMENT"),
    USER("USER")
}

enum class TypeReportedPost(val value:String) {
    NAKED("NAKED"),
    VIOLENCE("VIOLENCE"),
    TERRORISM("TERRORISM"),
    HARASSMENT("HARASSMENT"),
    SUICIDE("SUICIDE"),
    OTHER("OTHER")
}


