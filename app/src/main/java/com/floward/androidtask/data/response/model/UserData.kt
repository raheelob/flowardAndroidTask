package com.floward.androidtask.data.response.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Entity
@Parcelize
data class UserData (
    @NotNull
    @PrimaryKey
    @SerializedName("userId"       ) var userId       : Int?    = null,
    @SerializedName("albumId"      ) var albumId      : Int?    = null,
    @SerializedName("name"         ) var name         : String? = null,
    @SerializedName("url"          ) var url          : String? = null,
    @SerializedName("thumbnailUrl" ) var thumbnailUrl : String? = null) : Parcelable
{
    constructor() : this(0,0,"", "") // This is created for room...
}
