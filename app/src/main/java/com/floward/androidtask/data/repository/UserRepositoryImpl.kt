package com.floward.androidtask.data.repository

import android.util.Log
import com.floward.androidtask.data.api.APIsCollection
import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.local.UserDao
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

    override fun getUserPosts(
        fetchLocal: Boolean,
        userId: String,
    ): Flow<RemoteData<List<UserPostsData>>> = flow<RemoteData<List<UserPostsData>>> {
        if (fetchLocal) {
            val data = userDao.getAllUserPostsData(userId)
            emit(RemoteData.Success((data[0].userPostsData)))
        } else {
            val response = userAPI.getUserPost(userId = userId)
            //first delete all the record and then update with a new record...
//            userDao.deleteAllUserPostsData()
            userDao.insertAllUserPostsData(response)
            emit(RemoteData.Success(response))
        }
    }.flowOn(Dispatchers.IO)
}