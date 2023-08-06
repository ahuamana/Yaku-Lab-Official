package com.yakulab.domain



//InvalidYakuLapException
class InvalidYakuLapException : Exception{
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

data class GeneralErrorResponse(
    val error: Error
)

data class Error(
    val code: Int,
    val message: String
)