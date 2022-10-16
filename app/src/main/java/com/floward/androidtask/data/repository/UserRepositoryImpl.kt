package com.floward.androidtask.data.repository

import android.util.Log
import com.floward.androidtask.data.api.APIsCollection
import com.floward.androidtask.data.api.LocalData
import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.local.UserDao
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserPostsData
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

    override fun getAllPosts(): Flow<RemoteData<List<UserPostsData>>> = flow<RemoteData<List<UserPostsData>>> {
            val response = userAPI.getUserPost()
            //first delete all the record and then update with a new record...
//            userDao.deleteAllUserPostsData()
            userDao.insertAllUserPostsData(response)
            emit(RemoteData.Success(response))
    }.flowOn(Dispatchers.IO)

    override fun getUserAndTheirPost(): Flow<LocalData<List<UserAndTheirPostsData>>>  = flow<LocalData<List<UserAndTheirPostsData>>> {
        val response = userDao.getAllUserPostsData()
        emit((LocalData.SuccessFulRead( response)))
    }.flowOn(Dispatchers.IO)
}