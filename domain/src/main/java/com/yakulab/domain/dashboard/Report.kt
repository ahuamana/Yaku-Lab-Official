package com.yakulab.domain.dashboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Report (
    @SerialName("idPostReported")
    var idPostReported: String? = null,
    @SerialName("idChallengeReported")
    var idChallengeReported: String? = null,
    @SerialName("typeReport")
    var typeReport:String?=null,
    @SerialName("typeReportedPost")
    var typeReportedPost:String?=null,
    @SerialName("idCommentReported")
    var idCommentReported:String?=null,
    @SerialName("idPhotoReported")
    var idPhotoReported:String?=null,
    @SerialName("reportedComentario")
    var reportedComentario:String?=null,
    @SerialName("userReported")
    var userReported:String?=null,
    @SerialName("emailWhoReport")
    var emailWhoReport: String,
    @SerialName("lastNameWhoReport")
    var lastNameWhoReport: String,
    @SerialName("firstNameWhoReport")
    var firstNameWhoReport: String?,
    @SerialName("datetimeUnixTime")
    var datetimeUnixTime: Long? = null,
    @SerialName("datetime")
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


