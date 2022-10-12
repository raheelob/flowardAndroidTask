package com.floward.androidtask.data.repository

import com.floward.androidtask.data.api.APIsCollection
import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.local.UserDao
import com.floward.androidtask.data.response.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userAPI: APIsCollection,
    private val userDao: UserDao,
) : UserRepository {
    override fun getUserList(fetchLocal: Boolean): Flow<RemoteData<List<UserData>>> =
        flow {
            if (fetchLocal) {
                emit(RemoteData.Success((userDao.getAllUserData())))
            } else {
                val response = userAPI.getUsersList()
                //first delete all the record and then update with a new record...
                userDao.deleteAllUserData()
                userDao.insertAllUserData(response)
                emit(RemoteData.Success(response))
            }
        }.flowOn(Dispatchers.IO)
}