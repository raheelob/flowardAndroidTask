package com.floward.androidtask.data.api

sealed class LocalData<out R> {
    data class SuccessFulRead<out T>(val value: T) : LocalData<T>()
    data class ErrorReading(val exception: Exception) : LocalData<Nothing>()
    object Loading : LocalData<Nothing>()
}
