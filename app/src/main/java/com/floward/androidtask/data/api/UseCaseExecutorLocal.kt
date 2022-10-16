package com.floward.androidtask.data.api

import com.floward.androidtask.data.response.model.ErrorData
import com.floward.androidtask.utils.ErrorConvertor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import okio.IOException
import retrofit2.HttpException

abstract class UseCaseExecutorLocal<out R>() {

    abstract fun readDB(): Flow<LocalData<R>>

    fun readDataFromDB(): Flow<LocalData<R>> {
        return readDB().buffer().catch { e ->
            when (e) {
                is Exception ->
                    emit(LocalData.ErrorReading(e))
            }
        }
    }
}