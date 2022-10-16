package com.floward.androidtask.data.api

import com.floward.androidtask.data.events.UserDataEvent
import com.floward.androidtask.data.response.model.ErrorData
import java.lang.Exception

sealed class LocalData<out R> {
    data class SuccessFulRead<out T>(val value: T) : LocalData<T>()
    data class ErrorReading(val exception: Exception) : LocalData<Nothing>()
    object Loading : LocalData<Nothing>()
}
