package com.paparazziteam.yakulap.helper.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

suspend fun <T> performNetworkWithFlow(networkCall: suspend () -> T): Flow<T> =
    flow {
        val responseStatus = networkCall.invoke()
        emit(responseStatus)
    }.flowOn(Dispatchers.IO)

fun <T, A> performGetOperationFlow(
    databaseQuery: Flow<T>,
    networkCall: suspend () -> A,
    saveCallResult: suspend (A) -> Unit
): Flow<T> = flow {
    emitAll(databaseQuery.onEach {
        println("databaseQuery: $it")
        emit(it)
    })
    val responseStatus = networkCall.invoke()
    saveCallResult(responseStatus)

}.flowOn(Dispatchers.IO)