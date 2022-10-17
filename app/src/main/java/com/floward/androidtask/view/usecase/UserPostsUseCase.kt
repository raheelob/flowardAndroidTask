package com.floward.androidtask.view.usecase

import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.api.UseCaseExecutor
import com.floward.androidtask.data.repository.UserRepository
import com.floward.androidtask.data.response.model.UserPostsData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPostsUseCase @Inject constructor(private val userRepository: UserRepository) :
    UseCaseExecutor<UserPostsUseCase.Params, List<UserPostsData>>() {

    class Params {}

    override fun runUseCase(parameter: Params?): Flow<RemoteData<List<UserPostsData>>> {
        return userRepository.getAllPosts()
    }
}