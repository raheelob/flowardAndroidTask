package com.floward.androidtask.data.repository

import com.floward.androidtask.data.api.LocalData
import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserPostsData
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface UserRepository {
    fun getUserList(fetchLocal: Boolean): Flow<RemoteData<List<UserData>>>
    fun getAllPosts() : Flow<RemoteData<List<UserPostsData>>>
    fun getUserAndTheirPost() : Flow<LocalData<List<UserAndTheirPostsData>>>
}