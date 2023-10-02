package com.yakulab.domain.profile


//create a function to get the enum value from the domain
//1 -> tvType.text = "Certificado Oro"
//                    2 -> tvType.text = "Certificado Plata"
//                    3 -> tvType.text = "Certificado Bronce"
enum class EnumCertified(val value: Int) {
    GOLD(1),
    SILVER(2),
    BRONZE(3);

    fun getCertifiedName(): String {
        return when (this) {
            GOLD -> "Certificado Oro"
            SILVER -> "Certificado Plata"
            BRONZE -> "Certificado Bronce"
        }
    }

    fun getColorBackground(): String {
        return when (this) {
            GOLD -> "#20EAB021"
            SILVER -> "#2010101A"
            BRONZE -> "#20A59E8E"
        }
    }

    fun getColorText(): String {
        return when (this) {
            GOLD -> "#EDB552"
            SILVER -> "#778390"
            BRONZE -> "#A59E8E"
        }
    }
}



