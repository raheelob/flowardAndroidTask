package com.floward.androidtask.view.usecase

import com.floward.androidtask.data.api.LocalData
import com.floward.androidtask.data.api.UseCaseExecutorLocal
import com.floward.androidtask.data.repository.UserRepository
import com.floward.androidtask.data.response.model.UserAndTheirPostsData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserAndTheirPostsUseCase @Inject constructor(private val userRepository: UserRepository) :
    UseCaseExecutorLocal<List<UserAndTheirPostsData>>() {

    override fun readDB(): Flow<LocalData<List<UserAndTheirPostsData>>> {
        return userRepository.getUserAndTheirPost()
    }
}