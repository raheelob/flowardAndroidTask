package com.floward.androidtask.data.response.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserAndTheirPostsData(
    @Embedded val userData: UserData,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val userPostsData: List<UserPostsData>,
) : Parcelable
{
    constructor() : this(UserData(), emptyList()) // This is created for room...
}
