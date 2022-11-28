package com.paparazziteam.yakulap.modulos.dashboard.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeCompleted (
    @SerialName("id")
    var id: String? = null,

    @SerialName("name")
    var name: String? = null,

    @SerialName("tipo")
    var tipo: String? = null,

    @SerialName("url")
    var url: String? = null,

    @SerialName("author_email")
    var author_email:String?= null,

    @SerialName("author_name")
    var author_name:String?= null,

    @SerialName("author_lastname")
    var author_lastname:String?= null,

    @SerialName("timestamp")
    var timestamp: Long? = null,

    @SerialName("challenge_id")
    var challenge_id: String? = null,

    @SerialName("post_blocked")
    var post_blocked: List<String>? = null
)

enum class TypeGroup(val value:String) {
    ANIMALS("ANIMALS"),
    PLANTS("PLANTS"),
    FRUITS("FRUITS")
}