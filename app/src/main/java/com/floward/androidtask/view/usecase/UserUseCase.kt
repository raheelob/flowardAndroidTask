package com.floward.androidtask.view.usecase

import com.floward.androidtask.data.api.RemoteData
import com.floward.androidtask.data.api.UseCaseExecutor
import com.floward.androidtask.data.repository.UserRepository
import com.floward.androidtask.data.response.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(private val userRepository: UserRepository) :
    UseCaseExecutor<UserUseCase.Params, List<UserData>>() {
    override fun runUseCase(parameter: Params?): Flow<RemoteData<List<UserData>>> {
        return userRepository.getUserList()
    }

    class Params {}
}