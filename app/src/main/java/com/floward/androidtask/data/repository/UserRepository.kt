package com.floward.androidtask.data.repository

import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.response.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface UserRepository {
    fun getUserList(fetchLocal: Boolean): Flow<RemoteData<List<UserData>>>
}