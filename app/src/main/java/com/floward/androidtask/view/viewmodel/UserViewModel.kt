package com.floward.androidtask.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floward.androidtask.data.api.LocalData
import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.events.PostDataEvent
import com.floward.androidtask.data.events.UserAndTheirPostEvent
import com.floward.androidtask.data.events.UserDataEvent
import com.floward.androidtask.data.response.model.ErrorData
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.view.usecase.UserAndTheirPostsUseCase
import com.floward.androidtask.view.usecase.UserPostsUseCase
import com.floward.androidtask.view.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val userPostsUseCase: UserPostsUseCase,
    private val userAndTheirPostsUseCase: UserAndTheirPostsUseCase
) : ViewModel() {
    private var retrieveList:List<UserAndTheirPostsData> = emptyList()
    private var rvLastViewedPosition : Int = 0
    private var isConfig: Boolean = false
    //remote // users event...
    /***********************************************************************************/
    private val usersTasksEventChannel = Channel<UserDataEvent>()
    internal val usersTasksEvent = usersTasksEventChannel.receiveAsFlow()
    /***********************************************************************************/
    //remote // post event...
    /***********************************************************************************/
    private val postsTasksEventChannel = Channel<PostDataEvent>()
    internal val postsTasksEvent = postsTasksEventChannel.receiveAsFlow()
    /***********************************************************************************/
    //local // user and their posts...
    /***********************************************************************************/
    private val userAndTheirPostTasksEventChannel = Channel<UserAndTheirPostEvent>()
    internal val userAndTheirPostTasksEvent = userAndTheirPostTasksEventChannel.receiveAsFlow()
    /***********************************************************************************/
    init {
        getUserList()
    }
    /***********************************************************************************/
    //remote // users event...
    /***********************************************************************************/
     fun getUserList() = viewModelScope.launch {
        usersTasksEventChannel.send(UserDataEvent.Loading)
        userUseCase.execute(UserUseCase.Params()).collect { response ->
            when (response) {
                RemoteData.Loading -> sendLoadingEvent()

                is RemoteData.Success -> response.value.let {
                    if (it.isNotEmpty()) getUserListEvent(true) else getUserListEvent(false)
                }

                is RemoteData.RemoteErrorByNetwork -> sendRemoteErrorByNetworkEvent()

                is RemoteData.Error -> response.error?.let { sendErrorEvent(it) }
            }
        }
    }

    private suspend fun sendLoadingEvent() {
        usersTasksEventChannel.send(UserDataEvent.Loading)
    }

    private suspend fun sendRemoteErrorByNetworkEvent() {
        usersTasksEventChannel.send(UserDataEvent.RemoteErrorByNetwork)
    }

    internal suspend fun sendErrorEvent(errorData: ErrorData) {
        usersTasksEventChannel.send(UserDataEvent.Error(errorData))
    }

    internal suspend fun getUserListEvent(dataReceived: Boolean) {
        if (dataReceived) {
            usersTasksEventChannel.send(UserDataEvent.GetUserList(dataReceived = dataReceived))
        } else
            sendErrorEvent(ErrorData(ok = false, errorCode = 1, error = "List is empty"))
    }
    /***********************************************************************************/
    //remote // posts event...
    /***********************************************************************************/
    internal fun getPosts() = viewModelScope.launch {
        postsTasksEventChannel.send(PostDataEvent.Loading)
        userPostsUseCase.execute(UserPostsUseCase.Params()).collect { response ->
            when (response) {
                RemoteData.Loading -> sendPostsLoadingEvent()

                is RemoteData.Success -> response.value.let {
                    if (it.isNotEmpty()) getPostsList(true) else getPostsList(false)
                }

                is RemoteData.RemoteErrorByNetwork -> sendPostsRemoteErrorByNetworkEvent()

                is RemoteData.Error -> response.error?.let { sendPostsErrorEvent(it) }
            }
        }
    }

    private suspend fun sendPostsLoadingEvent() {
        postsTasksEventChannel.send(PostDataEvent.Loading)
    }

    private suspend fun sendPostsRemoteErrorByNetworkEvent() {
        postsTasksEventChannel.send(PostDataEvent.RemoteErrorByNetwork)
    }

    internal suspend fun sendPostsErrorEvent(errorData: ErrorData) {
        postsTasksEventChannel.send(PostDataEvent.Error(errorData))
    }

    internal suspend fun getPostsList(dataReceived: Boolean) {
        if (dataReceived) {
            postsTasksEventChannel.send(PostDataEvent.GetPosts(true))
        } else
            sendErrorEvent(ErrorData(ok = false, errorCode = 1, error = "List is empty"))
    }
    /***********************************************************************************/
    //local // user and their posts...
    /***********************************************************************************/
    internal fun getUserAndTheirPosts() = viewModelScope.launch {
        userAndTheirPostTasksEventChannel.send(UserAndTheirPostEvent.Loading)
        userAndTheirPostsUseCase.readDataFromDB().collect { response ->
            when (response) {
                is LocalData.SuccessFulRead -> response.value.let { userAndTheirPostsList ->
                    retrieveList = userAndTheirPostsList
                    getAllUsersAndTheirPostsList(userAndTheirPostsList)
                }
                is LocalData.ErrorReading -> sendUserAndTheirPostsErrorEvent(response.exception)

                is LocalData.Loading -> sendUserAndTheirPostsLoadingEvent()
            }
        }
    }

    private suspend fun sendUserAndTheirPostsLoadingEvent() {
        userAndTheirPostTasksEventChannel.send(UserAndTheirPostEvent.Loading)
    }

    internal suspend fun sendUserAndTheirPostsErrorEvent(ex: Exception) {
        userAndTheirPostTasksEventChannel.send(UserAndTheirPostEvent.Error(ex))
    }

    internal suspend fun getAllUsersAndTheirPostsList(list: List<UserAndTheirPostsData>) {
        userAndTheirPostTasksEventChannel.send(UserAndTheirPostEvent.UserWithTheirPosts(list))
    }
    /***********************************************************************************/
    //Configuration handling logic...
    /***********************************************************************************/
    internal fun setLastVisiblePosition(position :Int){
        rvLastViewedPosition = position
    }

    internal fun getLastVisiblePosition() = rvLastViewedPosition

    internal fun handleConfig(configMode: Boolean){
        isConfig = configMode
    }

    internal fun getConfig() = isConfig

    internal fun retrieveList() = viewModelScope.launch {
        getAllUsersAndTheirPostsList(retrieveList)
    }
    /***********************************************************************************/
}