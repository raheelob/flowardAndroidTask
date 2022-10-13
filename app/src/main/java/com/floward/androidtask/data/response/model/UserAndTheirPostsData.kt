package com.floward.androidtask.data.response.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndTheirPostsData(
    @Embedded val userData: UserData,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val userPostsData: List<UserPostsData>,
)