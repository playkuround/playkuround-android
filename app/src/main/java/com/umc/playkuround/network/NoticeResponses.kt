package com.umc.playkuround.network

import com.google.gson.annotations.SerializedName

data class NoticeResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("response") val response: List<NoticeInfo>
)

data class NoticeInfo(
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("imageUrl") val imageUrl : String?,
    @SerializedName("description") val description : String?,
    @SerializedName("referenceUrl") val referenceUrl : String?
)