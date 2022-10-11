package com.floward.androidtask.data.response.model

import com.google.gson.annotations.SerializedName

data class ErrorData(
    @SerializedName("ok") var ok: Boolean? = null,
    @SerializedName("error_code") var errorCode: Int? = null,
    @SerializedName("error") var error: String? = null
)