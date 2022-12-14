package com.floward.androidtask.data.api

import com.floward.androidtask.data.response.model.UserData
import com.floward.androidtask.data.response.model.UserPostsData
import retrofit2.http.GET

interface APIsCollection {
    @GET("users")
    suspend fun getUsersList(): List<UserData>

    @GET("posts")
    suspend fun getUserPost(): List<UserPostsData>
}