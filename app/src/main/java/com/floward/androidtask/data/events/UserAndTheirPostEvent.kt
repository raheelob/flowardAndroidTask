package com.floward.androidtask.data.events

import com.floward.androidtask.data.response.model.UserAndTheirPostsData

sealed class UserAndTheirPostEvent{
    data class UserWithTheirPosts(val list: List<UserAndTheirPostsData>) : UserAndTheirPostEvent()
    class Error(val exception: Exception) : UserAndTheirPostEvent()
    object Loading : UserAndTheirPostEvent()
}
