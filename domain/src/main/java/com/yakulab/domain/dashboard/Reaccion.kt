package com.yakulab.domain.dashboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reaccion (
    @SerialName("id") var id: String? = null,
    @SerialName("id_email") var id_email: String? = null,
    @SerialName("status") var status: Boolean = false,
    @SerialName("email") var email: String? = null,
    @SerialName("type") var type: String? = null
)