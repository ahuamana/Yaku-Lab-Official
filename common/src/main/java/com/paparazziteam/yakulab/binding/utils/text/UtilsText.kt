package com.paparazziteam.yakulab.binding.utils.text

fun String.capitalizeWords(): String {
    val regex = Regex("(^|\\s|\\-|\\.|\\,)\\p{L}")
    return this.lowercase().replace(regex) { it.value.uppercase() }
}