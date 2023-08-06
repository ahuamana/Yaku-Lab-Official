package com.yakulab.domain.dashboard

data class Comment (
    var id:String?=null,
    var id_photo:String?=null,
    var email:String?=null,
    var type:String?=null,
    var message:String?=null,
    var status:Boolean?= null,
    var timestamp:Long?= null
)


enum class TypeComment(val value:String) {
    Comentario("comment")
}

