package com.floward.androidtask.data.response.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Entity
@Parcelize
data class UserPostsData(
    @SerializedName("userId") var userId: Int? = null,
    @PrimaryKey
    @NotNull
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,
) : Parcelable
{
    constructor() : this(0,0,"", "") // This is created for room...
}
