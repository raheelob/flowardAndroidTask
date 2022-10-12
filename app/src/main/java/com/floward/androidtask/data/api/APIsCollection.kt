package com.floward.androidtask.data.api

import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserDetailData
import retrofit2.http.GET

interface APIsCollection {
    @GET("users")
    suspend fun getUsersList(): List<UserData>

    @GET("users")
    suspend fun getUserPost(): List<UserDetailData>
}