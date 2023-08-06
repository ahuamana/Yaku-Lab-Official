package com.yakulab.framework.network

import android.util.Log
import com.google.gson.Gson
import com.yakulab.domain.GeneralErrorResponse
import com.yakulab.domain.InvalidYakuLapException
import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getResultWithForce(call: suspend () -> Response<T>, forceError: Boolean = false): T {
        if(forceError){
            throw Exception("force error for testing purpose only -- BaseDataSource.kt")
        }
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return body
        }

        if(response.code() in 400..499){
            val errorResponse = Gson().fromJson(response.errorBody()?.string()?:"", GeneralErrorResponse::class.java)
            Log.e("BaseDataSource","Error 400 -- ${errorResponse.error.message}")
            throw InvalidYakuLapException(errorResponse.error.message)
        }

        throw Exception(" not e ${response.code()} ${response.body()}")
    }
}