package com.paparazziteam.yakulap.helper.network

import android.util.Log
import com.paparazziteam.yakulap.domain.main.GeneralErrorResponse
import com.paparazziteam.yakulap.domain.main.InvalidYakuLapException
import com.paparazziteam.yakulap.helper.fromJson
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
            val errorResponse = fromJson<GeneralErrorResponse>(response.errorBody()?.string()?:"")
            Log.e("BaseDataSource","Error 400 -- ${errorResponse.error.message}")
            throw InvalidYakuLapException(errorResponse.error.message)
        }

        throw Exception(" not e ${response.code()} ${response.body()}")
    }
}