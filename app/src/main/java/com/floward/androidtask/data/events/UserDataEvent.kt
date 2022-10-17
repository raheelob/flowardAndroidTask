package com.floward.androidtask.data.events

import com.floward.androidtask.data.response.model.ErrorData

sealed class UserDataEvent {
    data class GetUserList(val dataReceived: Boolean) : UserDataEvent()
    class Error(val errorData: ErrorData) : UserDataEvent()
    object RemoteErrorByNetwork : UserDataEvent()
    object Loading : UserDataEvent()
}
