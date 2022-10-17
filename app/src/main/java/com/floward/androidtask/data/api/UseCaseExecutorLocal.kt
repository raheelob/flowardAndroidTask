package com.floward.androidtask.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch

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