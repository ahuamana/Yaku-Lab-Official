package com.ahuaman.feature_ar.presentation.AR

import io.github.sceneview.model.ModelInstance
import java.io.BufferedReader

data class BaseState (
    val isLoading: Boolean = false,
    val isSuccess: List<StateARScreen> = emptyList(),
    val isError: Boolean = false,
)


sealed class StateARScreen{
    data class SuccessArray(val data: ByteArray) : StateARScreen() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SuccessArray

            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class SuccessModel(val modelInstance: ModelInstance?): StateARScreen()
}

