package com.paparazziteam.yakulap.presentation.login.pojo

data class User (
    var nombres: String? = null,
    var apellidos: String? = null,
    var alias: String? = null,
    var email: String? = null,
    var isBypass:String?= null,
    var isAdmin: Boolean = false,
    var isSuperAdmin: Boolean = false,
    var points: Int? = null,
    //new version
    var post_blocked: List<String>? = null,
    var users_blocked: List<String>? = null
)