package com.floward.androidtask.utils

import com.floward.androidtask.data.response.model.ErrorData
import com.google.gson.Gson
import retrofit2.HttpException

object ErrorConvertor {
     fun parseErrorBody(throwable: HttpException): ErrorData? {
        return try {
            val errorJsonString = throwable.response()?.errorBody()?.string()
            return Gson().fromJson(errorJsonString, ErrorData::class.java)
        } catch (exception: Exception) {
            null
        }
    }
}