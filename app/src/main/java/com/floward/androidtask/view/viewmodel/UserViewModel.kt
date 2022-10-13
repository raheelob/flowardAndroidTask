package com.floward.androidtask.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.response.model.ErrorData
import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserPostsData
import com.floward.androidtask.view.usecase.UserPostsUseCase
import com.floward.androidtask.view.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val userPostsUseCase: UserPostsUseCase
) : ViewModel(){
    //remote // users event...
    /***********************************************************************************/
    private val usersTasksEventChannel = Channel<UserDataEvent>()
    val usersTasksEvent = usersTasksEventChannel.receiveAsFlow()

    sealed class UserDataEvent {
        data class GetUserList(val list: List<UserData>) : UserDataEvent()
        class Error(val errorData: ErrorData) : UserDataEvent()
        object RemoteErrorByNetwork : UserDataEvent()
        object Loading : UserDataEvent()
    }
    /***********************************************************************************/
    //remote // users event...
    /***********************************************************************************/
    private val postsTasksEventChannel = Channel<PostDataEvent>()
    val postsTasksEvent = postsTasksEventChannel.receiveAsFlow()

    sealed class PostDataEvent {
        data class GetPosts(val list: List<UserPostsData>) : PostDataEvent()
        class Error(val errorData: ErrorData) : PostDataEvent()
        object RemoteErrorByNetwork : PostDataEvent()
        object Loading : PostDataEvent()
    }
    /***********************************************************************************/
    init {
        getUserList()
    }

    //remote // users event...
    /***********************************************************************************/
    private fun getUserList() = viewModelScope.launch {
        usersTasksEventChannel.send(UserDataEvent.Loading)
        userUseCase.execute(UserUseCase.Params(fetchLocal = false)).collect{ response ->
            when (response) {
                RemoteData.Loading -> {
                    sendLoadingEvent()
                }

                is RemoteData.Success -> {
                    response.value.let {
                        getUserList(response.value)
                    }
                }

                is RemoteData.RemoteErrorByNetwork -> {
                    sendRemoteErrorByNetworkEvent()
                }

                is RemoteData.Error -> {
                    response.error?.let { sendErrorEvent(it) }
                }
            }
        }
    }

    private suspend fun sendLoadingEvent() {
        usersTasksEventChannel.send(UserDataEvent.Loading)
    }

    private suspend fun sendRemoteErrorByNetworkEvent() {
        usersTasksEventChannel.send(UserDataEvent.RemoteErrorByNetwork)
    }

    suspend fun sendErrorEvent(errorData: ErrorData) {
        usersTasksEventChannel.send(UserDataEvent.Error(errorData))
    }

    suspend fun getUserList(list: List<UserData>) {
        if (list.isEmpty()) {
            sendErrorEvent(ErrorData(ok = false, errorCode = 1, error = "List is empty"))
        } else
            usersTasksEventChannel.send(UserDataEvent.GetUserList(list))
    }
    /***********************************************************************************/
    //remote // posts event...
    /***********************************************************************************/
    public fun getPosts() = viewModelScope.launch {
        postsTasksEventChannel.send(PostDataEvent.Loading)
        userPostsUseCase.execute(UserPostsUseCase.Params(fetchLocal = true, userId = "1")).collect{
             response ->
                when (response) {
                    RemoteData.Loading -> {
                        sendPostsLoadingEvent()
                    }

                    is RemoteData.Success -> {
                        response.value.let {
                            getPostsUserList(response.value)
                        }
                    }

                    is RemoteData.RemoteErrorByNetwork -> {
                        sendPostsRemoteErrorByNetworkEvent()
                    }

                    is RemoteData.Error -> {
                        response.error?.let { sendErrorEvent(it) }
                    }
            }
        }
    }

    private suspend fun sendPostsLoadingEvent() {
        postsTasksEventChannel.send(PostDataEvent.Loading)
    }

    private suspend fun sendPostsRemoteErrorByNetworkEvent() {
        postsTasksEventChannel.send(PostDataEvent.RemoteErrorByNetwork)
    }

    suspend fun sendPostsErrorEvent(errorData: ErrorData) {
        postsTasksEventChannel.send(PostDataEvent.Error(errorData))
    }

    suspend fun getPostsUserList(list: List<UserPostsData>) {
        if (list.isEmpty()) {
            sendErrorEvent(ErrorData(ok = false, errorCode = 1, error = "List is empty"))
        } else
            postsTasksEventChannel.send(PostDataEvent.GetPosts(list))
    }

    /***********************************************************************************/
}