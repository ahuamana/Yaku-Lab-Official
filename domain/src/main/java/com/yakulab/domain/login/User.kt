package com.yakulab.domain.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User (
    @SerialName("nombres") var nombres: String? = null,
    @SerialName("apellidos") var apellidos: String? = null,
    @SerialName("alias") var alias: String? = null,
    @SerialName("email") var email: String? = null,
    @SerialName("isBypass") var isBypass:String?= null,
    @SerialName("isAdmin") var isAdmin: Boolean = false,
    @SerialName("isSuperAdmin") var isSuperAdmin: Boolean = false,
    @SerialName("points") var points: Int? = null,
    //new version
    @SerialName("post_blocked") var post_blocked: List<String>? = null,
    @SerialName("users_blocked") var users_blocked: List<String>? = null
)