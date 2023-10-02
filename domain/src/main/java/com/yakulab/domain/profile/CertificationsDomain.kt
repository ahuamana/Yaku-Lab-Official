package com.yakulab.domain.profile

data class CertificationsDomain(
    val id: Int,
    val name: String,
    val icon: String,
    val color: String,
    val type: Int, // BRONZE, SILVER, GOLD
    val isCertified: Boolean = false,
)
