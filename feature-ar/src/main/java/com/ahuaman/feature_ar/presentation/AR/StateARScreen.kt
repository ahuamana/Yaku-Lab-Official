package com.ahuaman.feature_ar.presentation.AR

sealed class StateARScreen {
    object Loading : StateARScreen()
    data class Success(val byteArray: ByteArray) : StateARScreen() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            return byteArray.contentEquals(other.byteArray)
        }

        override fun hashCode(): Int {
            return byteArray.contentHashCode()
        }
    }

    object Error : StateARScreen()
}