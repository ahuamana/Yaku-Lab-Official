package com.paparazziteam.yakulap.modulos.dashboard.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comment (
    @SerialName("id")
    var id:String?=null,

    @SerialName("id_photo")
    var id_photo:String?=null,

    @SerialName("email")
    var email:String?=null,

    @SerialName("type")
    var type:String?=null,

    @SerialName("message")
    var message:String?=null,

    @SerialName("status")
    var status:Boolean?= null,

    @SerialName("timestamp")
    var timestamp:Long?= null
)


enum class TypeComment(val value:String) {
    Comentario("comment")
}

