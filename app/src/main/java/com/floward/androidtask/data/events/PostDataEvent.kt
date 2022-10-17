package com.floward.androidtask.data.events

import com.floward.androidtask.data.response.model.ErrorData

sealed class PostDataEvent {
    data class GetPosts(val dataReceived: Boolean) : PostDataEvent()
    class Error(val errorData: ErrorData) : PostDataEvent()
    object RemoteErrorByNetwork : PostDataEvent()
    object Loading : PostDataEvent()
}
